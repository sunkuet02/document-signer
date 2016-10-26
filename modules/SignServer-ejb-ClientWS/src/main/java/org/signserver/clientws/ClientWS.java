/*************************************************************************
 *                                                                       *
 *  SignServer: The OpenSource Automated Signing Server                  *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public           *
 *  License as published by the Free Software Foundation; either         *
 *  version 2.1 of the License, or any later version.                    *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/
package org.signserver.clientws;

import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.logging.Level;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;
import org.ejbca.util.CertTools;
import org.signserver.common.*;
import org.signserver.common.util.PropertiesConstants;
import org.signserver.ejb.interfaces.IGlobalConfigurationSession;
import org.signserver.ejb.interfaces.IWorkerSession;
import org.signserver.server.CertificateClientCredential;
import org.signserver.server.IClientCredential;
import org.signserver.server.UsernamePasswordClientCredential;
import org.signserver.server.log.AdminInfo;
import org.signserver.server.log.IWorkerLogger;
import org.signserver.server.log.LogMap;

/**
 * Client web services implementation containing operations for requesting
 * signing etc.
 *
 * @author Markus Kilås
 * @version $Id: ClientWS.java 4245 2014-02-06 15:49:26Z netmackan $
 */
@WebService(serviceName = "ClientWSService")
@Stateless()
public class ClientWS {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(ClientWS.class);

    private static final String HTTP_AUTH_BASIC_AUTHORIZATION = "Authorization";

    ClientWSHelper helper = new ClientWSHelper();

    @Resource
    private WebServiceContext wsContext;

    @EJB
    private IWorkerSession.ILocal workersession;

    private IWorkerSession.ILocal getWorkerSession() {
        return workersession;
    }

    private final Random random = new Random();

    /**
     * Generic operation for request signing of a byte array.
     *
     * @param workerIdOrName  Name or ID of worker to send the request to
     * @param requestMetadata Additional request meta data
     * @param data            The byte[] array with data in some format understood by the
     *                        worker
     * @return The response data
     * @throws RequestFailedException  In case the request could not be processed typically because some error in the request data.
     * @throws InternalServerException In case the request could not be processed by some error at the server side.
     */
    @WebMethod(operationName = "processData")
    public DataResponse processData(
            @WebParam(name = "worker") final String workerIdOrName,
            @WebParam(name = "metadata") List<Metadata> requestMetadata,
            @WebParam(name = "data") byte[] data) throws RequestFailedException, InternalServerException {
        final DataResponse result;
        try {
            final int workerId = getWorkerId(workerIdOrName);
            if (workerId < 1) {
                throw new RequestFailedException("No worker with the given name could be found");
            }
            final RequestContext requestContext = handleRequestContext(requestMetadata, workerId);

            final int requestId = random.nextInt();

            final ProcessRequest req = new GenericSignRequest(requestId, data);
            final ProcessResponse resp = getWorkerSession().process(workerId, req, requestContext);

            if (resp instanceof GenericSignResponse) {
                final GenericSignResponse signResponse = (GenericSignResponse) resp;
                if (signResponse.getRequestID() != requestId) {
                    LOG.error("Response ID " + signResponse.getRequestID() + " not matching request ID " + requestId);
                    throw new InternalServerException("Error in process operation, response id didn't match request id");
                }
                result = new DataResponse(requestId, signResponse.getProcessedData(), signResponse.getArchiveId(), signResponse.getSignerCertificate() == null ? null : signResponse.getSignerCertificate().getEncoded(), getResponseMetadata(requestContext));
            } else {
                LOG.error("Unexpected return type: " + resp.getClass().getName());
                throw new InternalServerException("Unexpected return type");
            }
        } catch (CertificateEncodingException ex) {
            LOG.error("Signer certificate could not be encoded", ex);
            throw new InternalServerException("Signer certificate could not be encoded");
        } catch (IllegalRequestException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            if (LOG.isDebugEnabled()) {
                LOG.info("Request failed: " + ex.getMessage(), ex);
            }
            throw new RequestFailedException(ex.getMessage());
        } catch (CryptoTokenOfflineException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service unvailable", ex);
            }
            throw new InternalServerException("Service unavailable: " + ex.getMessage());
        } catch (AuthorizationRequiredException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (AccessDeniedException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (SignServerException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Internal server error", ex);
            }
            throw new InternalServerException("Internal server error: " + ex.getMessage());
        }
        return result;
    }

    @WebMethod(operationName = "processBulkSOD")
    public BulkSODResponse processBulkSOD(
            @WebParam(name = "worker") final String workerIdOrName,
            @WebParam(name = "metadata") final List<Metadata> requestMetadata,
            @WebParam(name = "bulkSodData") final BulkSODRequest bulkSodData) throws RequestFailedException, InternalServerException {
        final BulkSODResponse result;
        try {
            final int workerId = getWorkerId(workerIdOrName);
            final RequestContext requestContext = handleRequestContext(requestMetadata, workerId);
            final int requestId = random.nextInt();

            List<SODRequestList> singleSodRequestData = bulkSodData.getSodRequestLists();

            final HashMap<String, Map<Integer, byte[]>> bulkSodDataGroup = new HashMap<String, Map<Integer, byte[]>>();

            for (SODRequestList sodRequest : singleSodRequestData) {
                final String sodDataGroupDocumentID = sodRequest.getSigningDocumentID();

                final HashMap<Integer, byte[]> sodDataGroup = new HashMap<Integer, byte[]>();
                for (DataGroup dataGroup : sodRequest.getDataGroups()) {
                    final int dataGroupId = dataGroup.getId();
                    if ((dataGroupId > -1) && (dataGroupId < 17)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Adding data group " + dataGroupId);
                            if (LOG.isTraceEnabled()) {
                                LOG.trace("with value " + dataGroup.getValue());
                            }
                        }
                        sodDataGroup.put(dataGroup.getId(), dataGroup.getValue());
                    } else {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Ignoring data group " + dataGroupId);
                        }
                    }
                }
                bulkSodDataGroup.put(sodDataGroupDocumentID, sodDataGroup);
            }

            String ldsVersion = bulkSodData.getLdsVersion();
            String unicodeVersion = bulkSodData.getUnicodeVersion();
            if (ldsVersion != null && ldsVersion.trim().isEmpty()) {
                ldsVersion = null;
            }
            if (unicodeVersion != null && unicodeVersion.trim().isEmpty()) {
                unicodeVersion = null;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested versions: LDS=" + ldsVersion
                        + ", Unicode=" + unicodeVersion);
            }

            final BulkSodSignRequest req = new BulkSodSignRequest(requestId, bulkSodDataGroup, ldsVersion, unicodeVersion);
            final ProcessResponse resp = getWorkerSession().process(workerId, req, requestContext);

            if (resp instanceof BulkSodSignResponse) {
                BulkSodSignResponse bulkSodSignResponse = (BulkSodSignResponse) resp;
                if (bulkSodSignResponse.getRequestID() != requestId) {
                    LOG.error("Response ID " + bulkSodSignResponse.getRequestID() + " not matching request ID " + requestId);
                    throw new SignServerException("Error in process operation, response id didn't match request id");
                }

                result = new BulkSODResponse(requestId, bulkSodSignResponse.getProcessedSODDataList().getProcessedDataList(),
                        bulkSodSignResponse.getNotProcessedSodDataList().getNotProcessedDataList(), bulkSodSignResponse.getSignerCertificate() == null ? null : bulkSodSignResponse.getSignerCertificate().getEncoded(), getResponseMetadata(requestContext));
            } else {
                LOG.error("Unexpected return type: " + resp.getClass().getName());
                throw new SignServerException("Unexpected return type");
            }

        } catch (CertificateEncodingException ex) {
            LOG.error("Certificate encoding error", ex);
            throw new InternalServerException("Internal server error");
        } catch (CryptoTokenOfflineException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service unvailable", ex);
            }
            throw new InternalServerException("Service unavailable: " + ex.getMessage());
        } catch (IllegalRequestException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (AuthorizationRequiredException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (AccessDeniedException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (SignServerException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Internal server error", ex);
            }
            throw new InternalServerException("Internal server error: " + ex.getMessage());
        }

        return result;
    }

    /**
     * Operation for requesting signing and production of an MRTD SOd based on
     * the supplied data groups / data group hashes.
     *
     * @param workerIdOrName  Name or ID of worker to send the request to
     * @param requestMetadata Additional request meta data
     * @param data            A SODRequest containing the datagroups/datagroups hashes
     * @return The response data
     * @throws RequestFailedException  In case the request could not be processed typically because some error in the request data.
     * @throws InternalServerException In case the request could not be processed by some error at the server side.
     */

    @WebMethod(operationName = "processSOD")
    public SODResponse processSOD(
            @WebParam(name = "worker") final String workerIdOrName,
            @WebParam(name = "metadata") final List<Metadata> requestMetadata,
            @WebParam(name = "sodData") final SODRequest data) throws RequestFailedException, InternalServerException {
        final SODResponse result;
        try {
            final int workerId = getWorkerId(workerIdOrName);
            final RequestContext requestContext = handleRequestContext(requestMetadata, workerId);
            final int requestId = random.nextInt();

            // Collect all [dataGroup1, dataGroup2, ..., dataGroupN]
            final List<DataGroup> dataGroups = data.getDataGroups();
            final HashMap<Integer, byte[]> dataGroupsMap = new HashMap<Integer, byte[]>();
            for (DataGroup dataGroup : dataGroups) {
                final int dataGroupId = dataGroup.getId();
                if ((dataGroupId > -1) && (dataGroupId < 17)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding data group " + dataGroupId);
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("with value " + dataGroup.getValue());
                        }
                    }
                    dataGroupsMap.put(dataGroup.getId(), dataGroup.getValue());
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Ignoring data group " + dataGroupId);
                    }
                }
            }
            if (data.getDataGroups().isEmpty()) {
                throw new RequestFailedException("Missing dataGroup fields in request");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Received number of dataGroups: " + dataGroups.size());
            }

            // LDS versioning
            String ldsVersion = data.getLdsVersion();
            String unicodeVersion = data.getUnicodeVersion();
            if (ldsVersion != null && ldsVersion.trim().isEmpty()) {
                ldsVersion = null;
            }
            if (unicodeVersion != null && unicodeVersion.trim().isEmpty()) {
                unicodeVersion = null;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requested versions: LDS=" + ldsVersion
                        + ", Unicode=" + unicodeVersion);
            }

            final SODSignRequest req = new SODSignRequest(requestId, dataGroupsMap, ldsVersion, unicodeVersion);
            final ProcessResponse resp = getWorkerSession().process(workerId, req, requestContext);

            if (resp instanceof SODSignResponse) {
                SODSignResponse signResponse = (SODSignResponse) resp;
                if (signResponse.getRequestID() != requestId) {
                    LOG.error("Response ID " + signResponse.getRequestID() + " not matching request ID " + requestId);
                    throw new SignServerException("Error in process operation, response id didn't match request id");
                }

                result = new SODResponse(requestId, signResponse.getProcessedData(), signResponse.getArchiveId(), signResponse.getSignerCertificate() == null ? null : signResponse.getSignerCertificate().getEncoded(), getResponseMetadata(requestContext));
            } else {
                LOG.error("Unexpected return type: " + resp.getClass().getName());
                throw new SignServerException("Unexpected return type");
            }
        } catch (CertificateEncodingException ex) {
            LOG.error("Certificate encoding error", ex);
            throw new InternalServerException("Internal server error");
        } catch (NoSuchWorkerException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Worker Not Found: " + ex.getWorkerIdOrName());
            }
            throw new RequestFailedException("Worker Not Found");
        } catch (CryptoTokenOfflineException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Service unvailable", ex);
            }
            throw new InternalServerException("Service unavailable: " + ex.getMessage());
        } catch (IllegalRequestException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (AuthorizationRequiredException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (AccessDeniedException ex) {
            LOG.info("Request failed: " + ex.getMessage());
            throw new RequestFailedException(ex.getMessage());
        } catch (SignServerException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Internal server error", ex);
            }
            throw new InternalServerException("Internal server error: " + ex.getMessage());
        }
        return result;
    }


    private String getRequestIP() {
        MessageContext msgContext = wsContext.getMessageContext();
        HttpServletRequest request = (HttpServletRequest) msgContext.get(MessageContext.SERVLET_REQUEST);

        return request.getRemoteAddr();
    }

    private X509Certificate getClientCertificate() {
        MessageContext msgContext = wsContext.getMessageContext();
        HttpServletRequest request = (HttpServletRequest) msgContext.get(MessageContext.SERVLET_REQUEST);
        X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

        if (certificates != null) {
            return certificates[0];
        }
        return null;
    }

    private int getWorkerId(String workerIdOrName) {
        final int retval;

        if (workerIdOrName.substring(0, 1).matches("\\d")) {
            retval = Integer.parseInt(workerIdOrName);
        } else {
            retval = getWorkerSession().getWorkerId(workerIdOrName);
        }
        return retval;
    }

    private RequestContext handleRequestContext(final List<Metadata> requestMetadata, final int workerId) {
        final HttpServletRequest servletRequest =
                (HttpServletRequest) wsContext.getMessageContext().get(MessageContext.SERVLET_REQUEST);
        String requestIP = getRequestIP();
        X509Certificate clientCertificate = getClientCertificate();
        final RequestContext requestContext = new RequestContext(clientCertificate, requestIP);

        IClientCredential credential;

        if (clientCertificate instanceof X509Certificate) {
            final X509Certificate cert = (X509Certificate) clientCertificate;
            LOG.debug("Authentication: certificate");
            credential = new CertificateClientCredential(
                    cert.getSerialNumber().toString(16),
                    cert.getIssuerDN().getName());
        } else {
            // Check is client supplied basic-credentials
            final String authorization = servletRequest.getHeader(
                    HTTP_AUTH_BASIC_AUTHORIZATION);
            if (authorization != null) {
                LOG.debug("Authentication: password");

                final String decoded[] = new String(Base64.decode(
                        authorization.split("\\s")[1])).split(":", 2);

                credential = new UsernamePasswordClientCredential(
                        decoded[0], decoded[1]);
            } else {
                LOG.debug("Authentication: none");
                credential = null;
            }
        }
        requestContext.put(RequestContext.CLIENT_CREDENTIAL, credential);

        final LogMap logMap = LogMap.getInstance(requestContext);

        // Add HTTP specific log entries
        logMap.put(IWorkerLogger.LOG_REQUEST_FULLURL,
                servletRequest.getRequestURL().append("?")
                        .append(servletRequest.getQueryString()).toString());
        logMap.put(IWorkerLogger.LOG_REQUEST_LENGTH,
                servletRequest.getHeader("Content-Length"));
        logMap.put(IWorkerLogger.LOG_XFORWARDEDFOR,
                servletRequest.getHeader("X-Forwarded-For"));

        logMap.put(IWorkerLogger.LOG_WORKER_NAME,
                getWorkerSession().getCurrentWorkerConfig(workerId).getProperty(PropertiesConstants.NAME));

        if (requestMetadata == null) {
            requestContext.remove(RequestContext.REQUEST_METADATA);
        } else {
            final RequestMetadata metadata = RequestMetadata.getInstance(requestContext);
            for (Metadata rmd : requestMetadata) {
                metadata.put(rmd.getName(), rmd.getValue());
            }

            // Special handling of FILENAME
            String fileName = metadata.get(RequestContext.FILENAME);
            if (fileName != null) {
                requestContext.put(RequestContext.FILENAME, fileName);
                logMap.put(IWorkerLogger.LOG_FILENAME, fileName);
            }
        }

        return requestContext;
    }

    private List<Metadata> getResponseMetadata(final RequestContext requestContext) {
        final LinkedList<Metadata> result = new LinkedList<Metadata>();
        // TODO: DSS-x: Implement support for "Response Metadata":
        //Object o = requestContext.get(RequestContext.REQUEST_METADATA);
        //if (o instanceof Map) {
        //    Map<String, String> requestMetadata = (Map<String, String>) o;
        //    for (Map.Entry<String, String> entry : requestMetadata.entrySet()) {
        //        result.add(new Metadata(entry.getKey(), entry.getValue()));
        //    }
        //}
        return result;
    }

    @WebMethod(operationName = "createCryptokeyWorker")
    public CryptoWorkerResponse createCryptokeyWorker(
            @WebParam(name = "name") final String workerName,
            @WebParam(name = "defaultKey") final String defaultKey,
            @WebParam(name = "slotLabelValue") final Integer slotLabelValue,
            @WebParam(name = "pin") final String pin,
            @WebParam(name = "algorithm") final String algorithm,
            @WebParam(name = "keyspec") final String keyspec) throws Exception {
        try {
            int workerId = getWorkerSession().genFreeWorkerId();
            helper.getGlobalConfigurationSession().setProperty(helper.SCOPE_GLOB, helper.WORKER_TEXT+workerId+helper.CLASSPATH_TEXT, helper.CRYPTOWORKER_CLASSPATH);
            helper.getGlobalConfigurationSession().setProperty(helper.SCOPE_GLOB,helper.WORKER_TEXT+workerId+helper.SIGNERTOKEN_CLASSPATH_TEXT, helper.PKCS11_CRYPTOWORKER_SIGNERTOKEN_CLASSPATH);

            getWorkerSession().setWorkerProperty(workerId, "NAME", workerName);
            getWorkerSession().setWorkerProperty(workerId,"SHAREDLIBRARYNAME", "Utimaco");
            getWorkerSession().setWorkerProperty(workerId,"SLOTLABELTYPE", "SLOT_NUMBER");
            getWorkerSession().setWorkerProperty(workerId,"SLOTLABELVALUE", String.valueOf(slotLabelValue) );
            getWorkerSession().setWorkerProperty(workerId,"PIN", pin);
            getWorkerSession().setWorkerProperty(workerId,"DEFAULTKEY", defaultKey );

            getWorkerSession().reloadConfiguration(workerId);
            Certificate certificate = getWorkerSession().generateSignerKeyAndGetCertificate(workerId, algorithm, keyspec, defaultKey, pin.toCharArray());
            getWorkerSession().activateSigner(workerId, pin);

            return new CryptoWorkerResponse(workerId, certificate.getEncoded());
        } catch (Exception e) {
            throw new Exception("Failed to create Worker. Cause : " + e.getMessage());
        }
    }

    @WebMethod(operationName = "createMrtdSodSigner")
    public MRTDSODSignerResponse createMrtdSodSigner(
            @WebParam(name = "name") final String workerName,
            @WebParam(name = "defaultKey") final String defaultKey,
            @WebParam(name = "slotLabelValue") final Integer slotLabelValue,
            @WebParam(name = "pin") final String pin,
            @WebParam(name = "algorithm") final String algorithm,
            @WebParam(name = "keyspec") final String keyspec) throws Exception {

        try {
            CryptoWorkerResponse cryptoWorkerResponse = createCryptokeyWorker(workerName + "CryptoToken", defaultKey, slotLabelValue, pin, algorithm, keyspec);

            int workerId = getWorkerSession().genFreeWorkerId();
            helper.getGlobalConfigurationSession().setProperty(helper.SCOPE_GLOB, helper.WORKER_TEXT+workerId+helper.CLASSPATH_TEXT , helper.MRTDSODSIGNER_CLASSPATH );
            getWorkerSession().setWorkerProperty(workerId,"NAME", workerName);
            getWorkerSession().setWorkerProperty(workerId,"CRYPTOTOKEN", workerName + "CryptoToken" );
            getWorkerSession().setWorkerProperty(workerId,"DEFAULTKEY", defaultKey);
            getWorkerSession().setWorkerProperty(workerId,"AUTHTYPE", "NOAUTH");
            getWorkerSession().setWorkerProperty(workerId,"DIGESTALGORITHM", "SHA256");
            getWorkerSession().setWorkerProperty(workerId,"SIGNATUREALGORITHM", algorithm);
            getWorkerSession().setWorkerProperty(workerId,"DODATAGROUPHASHING", "true");

            getWorkerSession().reloadConfiguration(workerId);
            Certificate cert = CertTools.getCertfromByteArray(cryptoWorkerResponse.getCertificate());
            List<byte[]> certChain = new ArrayList<byte[]>();
            certChain.add(cert.getEncoded());

            getWorkerSession().uploadSignerCertificate(workerId, cert.getEncoded(), helper.SCOPE_GLOB);
            getWorkerSession().uploadSignerCertificateChain(workerId, certChain, helper.SCOPE_GLOB);

            getWorkerSession().reloadConfiguration(workerId);

            return new MRTDSODSignerResponse(workerId, cert.getPublicKey().getEncoded());

        } catch (Exception e) {
            throw new Exception("Failed to create MRTD SOD Signer, Cause : " + e.getMessage());
        }
    }

    @WebMethod(operationName = "decryptByteData")
    public DecryptDataResponse decryptByteData(
            @WebParam(name = "workerId") final int workerId,
            @WebParam(name = "encryptedData") final byte[] encryptedData) throws Exception {

        String decryptByteData = getWorkerSession().decryptByteData(workerId - 1, encryptedData);
        String result = "failed";
        if (encryptedData != null) {
            result = "success";
        }
        return new DecryptDataResponse(result, decryptByteData);
    }

    @WebMethod(operationName = "encryptMessage")
    public EncryptedMessageResponse encryptMessage(
            @WebParam(name = "workerId") final int workerId,
            @WebParam(name = "message") final String message) throws Exception {

        byte[] encryptedMessage = getWorkerSession().encryptMessage(workerId - 1, message);
        String result = "failed";
        if (encryptedMessage != null) {
            result = "success";
        }
        return new EncryptedMessageResponse(result, encryptedMessage);
    }
}