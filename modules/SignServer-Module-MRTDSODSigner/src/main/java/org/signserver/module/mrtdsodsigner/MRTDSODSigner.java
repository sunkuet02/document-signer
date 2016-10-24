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
package org.signserver.module.mrtdsodsigner;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.*;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX500NameUtil;
import org.bouncycastle.jcajce.provider.digest.Tiger;
import org.ejbca.util.CertTools;
import org.signserver.common.*;
import org.signserver.module.mrtdsodsigner.jmrtd.SODFile;
import org.signserver.server.WorkerContext;
import org.signserver.server.archive.Archivable;
import org.signserver.server.archive.DefaultArchivable;
import org.signserver.server.cryptotokens.ICryptoInstance;
import org.signserver.server.cryptotokens.ICryptoToken;
import org.signserver.server.signers.BaseSigner;

import com.tigerit.mrtd.*;

/**
 * A Signer creating a signed Security Object Data (SOD) file to be stored in ePassports.
 * <p>
 * Properties:
 * <ul>
 * <li>DIGESTALGORITHM = Message digest algorithm that is applied or should be applied to the values. (Optional)</li>
 * <li>SIGNATUREALGORITHM = Signature algorithm for signing the SO(d), should match
 * the digest algorithm. (Optional)</li>
 * <li>DODATAGROUPHASHING = True if this signer first should hash to values. Otherwise
 * the values are assumed to be hashes</li>
 * <li>LDSVERSION = Version of Logical Data Structure (LDS). For LDS version 1.7 enter "0107" and for version 1.8 "0108". (Optional, default is 0107)</li>
 * <li>UNICODEVERSION = Version of Unicode used in the datagroups. Required if LDS 1.8 is used. Example: "040000" for Unicode version 4.0.0.</li>
 * </ul>
 *
 * @author Tomas Gustavsson
 * @author Markus Kilås
 * @version $Id: MRTDSODSigner.java 5977 2015-03-27 10:30:50Z netmackan $
 */
public class MRTDSODSigner extends BaseSigner {

    private static final Logger log = Logger.getLogger(MRTDSODSigner.class);

    /**
     * The digest algorithm, for example SHA1, SHA256. Defaults to SHA256.
     */
    private static final String PROPERTY_DIGESTALGORITHM = "DIGESTALGORITHM";

    /**
     * Default value for the digestAlgorithm property
     */
    private static final String DEFAULT_DIGESTALGORITHM = "SHA256";

    /**
     * The signature algorithm, for example SHA1withRSA, SHA256withRSA, SHA256withECDSA. Defaults to SHA256withRSA.
     */
    private static final String PROPERTY_SIGNATUREALGORITHM = "SIGNATUREALGORITHM";

    /**
     * Default value for the signature algorithm property
     */
    private static final String DEFAULT_SIGNATUREALGORITHM = "SHA256withRSA";

    /**
     * Determines if the the data group values should be hashed by the signer. If false we assume they are already hashed.
     */
    private static final String PROPERTY_DODATAGROUPHASHING = "DODATAGROUPHASHING";

    /**
     * Default value if the data group values should be hashed by the signer.
     */
    private static final String DEFAULT_DODATAGROUPHASHING = "false";

    /**
     * Determines which version of the LDS to use.
     */
    private static final String PROPERTY_LDSVERSION = "LDSVERSION";

    /**
     * Default value if the LDS version is not specified.
     */
    private static final String DEFAULT_LDSVERSION = "0107";

    /**
     * Determines which version of Unicode to set.
     */
    private static final String PROPERTY_UNICODEVERSION = "UNICODEVERSION";

    private static final Object syncObj = new Object();

    private List<String> configErrors;

    @Override
    public void init(int workerId, WorkerConfig config,
                     WorkerContext workerContext, EntityManager workerEM) {
        super.init(workerId, config, workerContext, workerEM);

        configErrors = new LinkedList<String>();

        if (hasSetIncludeCertificateLevels) {
            configErrors.add(WorkerConfig.PROPERTY_INCLUDE_CERTIFICATE_LEVELS + " is not supported.");
        }
    }

    @Override
    public ProcessResponse processData(ProcessRequest signRequest, RequestContext requestContext) throws IllegalRequestException, CryptoTokenOfflineException, SignServerException {
        if (log.isTraceEnabled()) {
            log.trace(">processData");
        }
        ProcessResponse ret = null;

        // Check that the request contains a valid SODSignRequest object.
        if (signRequest instanceof SODSignRequest) {
            //throw new IllegalRequestException("Recieved request wasn't an expected SODSignRequest.");

            final ISignRequest sReq = (ISignRequest) signRequest;

            final SODSignRequest sodRequest = (SODSignRequest) signRequest;

            final ICryptoToken token = getCryptoToken();
            // Trying to do a workaround for issue when the PKCS#11 session becomes invalid
            // If autoactivate is on, we can deactivate and re-activate the token.
            synchronized (syncObj) {
                int status = token.getCryptoTokenStatus();
                if (log.isDebugEnabled()) {
                    log.debug("Crypto token status: " + status);
                }
                if (status != WorkerStatus.STATUS_ACTIVE) {
                    log.info("Crypto token status is not active, will see if we can autoactivate.");
                    String pin = config.getProperty("PIN");
                    if (pin == null) {
                        pin = config.getProperty("pin");
                    }
                    if (pin != null) {
                        log.info("Deactivating and re-activating crypto token.");
                        token.deactivate();
                        try {
                            token.activate(pin);
                        } catch (CryptoTokenAuthenticationFailureException e) {
                            throw new CryptoTokenOfflineException(e);
                        }
                    } else {
                        log.info("Autoactivation not enabled, can not re-activate crypto token.");
                    }
                }
            }

            // Construct SOD
            final SODFile sod;
            final X509Certificate cert;
            final List<Certificate> certChain;
            ICryptoInstance crypto = null;
            try {
                crypto = acquireCryptoInstance(ICryptoToken.PURPOSE_SIGN, signRequest, requestContext);



                cert = (X509Certificate) getSigningCertificate(crypto);
                if (cert == null) {
                    throw new CryptoTokenOfflineException("No signing certificate");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Using signer certificate with subjectDN '"
                            + CertTools.getSubjectDN(cert)
                            + "', issuerDN '"
                            + CertTools.getIssuerDN(cert)
                            + ", serNo "
                            + CertTools.getSerialNumberAsString(cert));
                }
                certChain = getSigningCertificateChain(crypto);

                // Create the SODFile using the data group hashes that was sent to us in the request.
                final String digestAlgorithm = config.getProperty(PROPERTY_DIGESTALGORITHM, DEFAULT_DIGESTALGORITHM);
                final String digestEncryptionAlgorithm = config.getProperty(PROPERTY_SIGNATUREALGORITHM, DEFAULT_SIGNATUREALGORITHM);
                if (log.isDebugEnabled()) {
                    log.debug("Using algorithms " + digestAlgorithm + ", " + digestEncryptionAlgorithm);
                }
                final String doHashing = config.getProperty(PROPERTY_DODATAGROUPHASHING, DEFAULT_DODATAGROUPHASHING);
                final Map<Integer, byte[]> dgvalues = sodRequest.getDataGroupHashes();
                Map<Integer, byte[]> dghashes = dgvalues;
                if (StringUtils.equalsIgnoreCase(doHashing, "true")) {
                    if (log.isDebugEnabled()) {
                        log.debug("Converting data group values to hashes using algorithm " + digestAlgorithm);
                    }
                    // If true here the "data group hashes" are not really hashes but values that we must hash.
                    // The input is already decoded (if needed) and nice, so we just need to hash it
                    dghashes = new HashMap<Integer, byte[]>(16);
                    for (Map.Entry<Integer, byte[]> dgId : dgvalues.entrySet()) {
                        final byte[] value = dgId.getValue();
                        if (log.isDebugEnabled()) {
                            log.debug("Hashing data group " + dgId + ", value is of length: " + value.length);
                        }
                        if ((value != null) && (value.length > 0)) {
                            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
                            byte[] result = digest.digest(value);
                            if (log.isDebugEnabled()) {
                                log.debug("Resulting hash is of length: " + result.length);
                            }
                            dghashes.put(dgId.getKey(), result);
                        }
                    }
                }

                // Version values from configuration
                String ldsVersion = config.getProperty(PROPERTY_LDSVERSION,
                        DEFAULT_LDSVERSION);
                String unicodeVersion = config.getProperty(PROPERTY_UNICODEVERSION);

                // Version values in request overrides configuration
                final String ldsVersionRequest = sodRequest.getLdsVersion();
                if (ldsVersionRequest != null) {
                    ldsVersion = ldsVersionRequest;
                }
                final String unicodeVersionRequest = sodRequest.getUnicodeVersion();
                if (unicodeVersionRequest != null) {
                    unicodeVersion = unicodeVersionRequest;
                }

                // Check version
                if ("0107".equals(ldsVersion)) {
                    // LDS V1.7 does not supported the version fields
                    ldsVersion = null;
                    unicodeVersion = null;
                } else if ("0108".equals(ldsVersion)) {
                    // LDS V1.8 requires a unicode version
                    if (unicodeVersion == null) {
                        throw new IllegalRequestException(
                                "Unicode version must be specified in LDS version 1.8");
                    }
                } else {
                    throw new IllegalRequestException(
                            "Unsupported LDS version: " + ldsVersion);
                }
                if (log.isDebugEnabled()) {
                    log.debug("LDS version: " + ldsVersion
                            + ", unicodeVerison: " + unicodeVersion);
                }
                // TODO : Sun : This should be uncomment
            /*
            final SODFile constructedSod
                    = new SODFile(digestAlgorithm, digestEncryptionAlgorithm,
                    dghashes, crypto.getPrivateKey(), cert, crypto.getProvider().getName(),
                    ldsVersion, unicodeVersion);
            */
                // TODO : Sun : This should be removed
                byte[] constructedSodEncodedData = getSodDataFromTigeritJars(dgvalues, crypto.getPrivateKey(), cert);
                sod = new SODFile(new ByteArrayInputStream(constructedSodEncodedData));
                // Reconstruct the sod
                //sod = new SODFile(new ByteArrayInputStream(constructedSod.getEncoded()));

            } catch (NoSuchAlgorithmException ex) {
                throw new SignServerException("Problem constructing SOD", ex);
            } catch (TigerSignerException ex) {
                throw new SignServerException("Problem constructing SOD", ex);
            } catch (IOException ex) {
                throw new SignServerException("Problem reconstructing SOD", ex);
            } finally {
                releaseCryptoInstance(crypto, requestContext);
            }

            // Verify the Signature before returning
            try {
                verifySignatureAndChain(sod, certChain);

                if (log.isDebugEnabled()) {
                    log.debug("SOD verified correctly, returning SOD.");
                }
                // Return response
                final byte[] signedbytes = sod.getEncoded();
                final String archiveId = createArchiveId(signedbytes, (String) requestContext.get(RequestContext.TRANSACTION_ID));
                final Collection<? extends Archivable> archivables = Arrays.asList(new DefaultArchivable(Archivable.TYPE_RESPONSE, signedbytes, archiveId));
                ret = new SODSignResponse(sReq.getRequestID(), signedbytes, cert,
                        archiveId, archivables);

                // The client can be charged for the request
                requestContext.setRequestFulfilledByWorker(true);
            } catch (TigerSignerException e) {
                log.error("Error verifying the SOD we signed ourselves. ", e);
                throw new SignServerException("SOD verification failure", e);
            } catch (IOException e) {
                log.error("Error encoding SOD", e);
                throw new SignServerException("SOD encoding failure", e);
            }

            if (log.isTraceEnabled()) {
                log.trace("<processData");
            }
        }

        // TODO : SUN : Check  : This is a added method for BulkSodSignResponse

        else if (signRequest instanceof BulkSodSignRequest) {
            final ISignRequest sReq = (ISignRequest) signRequest;

            final BulkSodSignRequest bulkSodSignRequest = (BulkSodSignRequest) signRequest;


            final ICryptoToken token = getCryptoToken();
            // Trying to do a workaround for issue when the PKCS#11 session becomes invalid
            // If autoactivate is on, we can deactivate and re-activate the token.
            synchronized (syncObj) {
                int status = token.getCryptoTokenStatus();
                if (log.isDebugEnabled()) {
                    log.debug("Crypto token status: " + status);
                }
                if (status != WorkerStatus.STATUS_ACTIVE) {
                    log.info("Crypto token status is not active, will see if we can autoactivate.");
                    String pin = config.getProperty("PIN");
                    if (pin == null) {
                        pin = config.getProperty("pin");
                    }
                    if (pin != null) {
                        log.info("Deactivating and re-activating crypto token.");
                        token.deactivate();
                        try {
                            token.activate(pin);
                        } catch (CryptoTokenAuthenticationFailureException e) {
                            throw new CryptoTokenOfflineException(e);
                        }
                    } else {
                        log.info("Autoactivation not enabled, can not re-activate crypto token.");
                    }
                }
            }

            // Construct SOD
            final X509Certificate cert;
            final List<Certificate> certChain;
            Map<String, SODFile> bulkSodData = new HashMap<String, SODFile>();
            List<NotProcessedSodData> notProcessedSodData = new ArrayList<NotProcessedSodData>();

            ICryptoInstance crypto = null;
            try {
                crypto = acquireCryptoInstance(ICryptoToken.PURPOSE_SIGN, signRequest, requestContext);

                cert = (X509Certificate) getSigningCertificate(crypto);
                if (cert == null) {
                    throw new CryptoTokenOfflineException("No signing certificate");
                }
                if (log.isDebugEnabled()) {
                    log.debug("Using signer certificate with subjectDN '"
                            + CertTools.getSubjectDN(cert)
                            + "', issuerDN '"
                            + CertTools.getIssuerDN(cert)
                            + ", serNo "
                            + CertTools.getSerialNumberAsString(cert));
                }
                certChain = getSigningCertificateChain(crypto);

                // Create the SODFile using the data group hashes that was sent to us in the request.
                final String digestAlgorithm = config.getProperty(PROPERTY_DIGESTALGORITHM, DEFAULT_DIGESTALGORITHM);
                final String digestEncryptionAlgorithm = config.getProperty(PROPERTY_SIGNATUREALGORITHM, DEFAULT_SIGNATUREALGORITHM);
                if (log.isDebugEnabled()) {
                    log.debug("Using algorithms " + digestAlgorithm + ", " + digestEncryptionAlgorithm);
                }
                final String doHashing = config.getProperty(PROPERTY_DODATAGROUPHASHING, DEFAULT_DODATAGROUPHASHING);
                final Map<String, Map<Integer, byte[]>> bulkDataGroups = bulkSodSignRequest.getBulkDataGroups();

                // Version values from configuration
                String ldsVersion = config.getProperty(PROPERTY_LDSVERSION,
                        DEFAULT_LDSVERSION);
                String unicodeVersion = config.getProperty(PROPERTY_UNICODEVERSION);

                // Version values in request overrides configuration
                final String ldsVersionRequest = bulkSodSignRequest.getLdsVersion();
                if (ldsVersionRequest != null) {
                    ldsVersion = ldsVersionRequest;
                }
                final String unicodeVersionRequest = bulkSodSignRequest.getUnicodeVersion();
                if (unicodeVersionRequest != null) {
                    unicodeVersion = unicodeVersionRequest;
                }

                // Check version
                if ("0107".equals(ldsVersion)) {
                    // LDS V1.7 does not supported the version fields
                    ldsVersion = null;
                    unicodeVersion = null;
                } else if ("0108".equals(ldsVersion)) {
                    // LDS V1.8 requires a unicode version
                    if (unicodeVersion == null) {
                        throw new IllegalRequestException(
                                "Unicode version must be specified in LDS version 1.8");
                    }
                } else {
                    throw new IllegalRequestException(
                            "Unsupported LDS version: " + ldsVersion);
                }
                if (log.isDebugEnabled()) {
                    log.debug("LDS version: " + ldsVersion
                            + ", unicodeVerison: " + unicodeVersion);
                }
                // TODO : Sun : This should be uncomment
            /*
            final SODFile constructedSod
                    = new SODFile(digestAlgorithm, digestEncryptionAlgorithm,
                    dghashes, crypto.getPrivateKey(), cert, crypto.getProvider().getName(),
                    ldsVersion, unicodeVersion);
            */
                // TODO : Sun : This should be removed
                for (Map.Entry<String, Map<Integer, byte[]>> entry : bulkDataGroups.entrySet()) {
                    SODFile sod = null;
                    NotProcessedSodData notProcessedData = new NotProcessedSodData(entry.getKey());

                    try {
                        byte[] constructedSodEncodedData = getSodDataFromTigeritJars(entry.getValue(), crypto.getPrivateKey(), cert);
                        sod = new SODFile(new ByteArrayInputStream(constructedSodEncodedData));
                        bulkSodData.put(entry.getKey(), sod);
                    } catch (TigerSignerException e) {
                        notProcessedData.setExceptionMessage(e.getMessage());
                        notProcessedSodData.add(notProcessedData);
                    } catch (IOException e) {
                        notProcessedData.setExceptionMessage(e.getMessage());
                        notProcessedSodData.add(notProcessedData);
                    }
                }
            } finally {
                releaseCryptoInstance(crypto, requestContext);
            }

            // Verify the Signature before returning

            List<SODData> bulkSignedBytes = new ArrayList<SODData>();

            for (Map.Entry<String, SODFile> sodData : bulkSodData.entrySet()) {
                try {
                    verifySignatureAndChain(sodData.getValue(), certChain);

                    if (log.isDebugEnabled()) {
                        log.debug("SOD verified correctly, returning SOD.");
                    }

                    final byte[] signedbytes = sodData.getValue().getEncoded();

                    SODData data = new SODData(sodData.getKey(), signedbytes);
                    bulkSignedBytes.add(data);
                } catch (Exception e) {
                    NotProcessedSodData notProcessedData = new NotProcessedSodData(sodData.getKey(), e.getMessage());
                    notProcessedSodData.add(notProcessedData);
                }
            }
            ret = new BulkSodSignResponse(sReq.getRequestID(), new ProcessedSODDataList(bulkSignedBytes), new NotProcessedSodDataList(notProcessedSodData), cert);

            // The client can be charged for the request
            requestContext.setRequestFulfilledByWorker(true);

        } else {
            throw new IllegalRequestException("Recieved request wasn't an expected SODSignRequest.");
        }
        return ret;
    }


    private byte[] getSodDataFromTigeritJars(Map<Integer, byte[]> dgValues, PrivateKey privateKey, X509Certificate certificate) throws TigerSignerException {
        byte[] sodData = null;
        IsoDataProcessor mdp = new IsoDataProcessor();
        Response response = new Response();
        DgContents dgContents = getLDSData(dgValues);
        byte[] dataToBeSigned = mdp.GetDataForSigning(dgContents, response);
        try {
            if (response.IsSuccessful) {
                byte[] signedData = getSampleSignedData(dataToBeSigned, privateKey);
                SodSecurityInfo ldsSecurityInfo = new SodSecurityInfo();

                ldsSecurityInfo.Certificate = certificate;
                ldsSecurityInfo.SignatureData = signedData;

                sodData = mdp.GetSod(ldsSecurityInfo, response);
                if (response.IsSuccessful) {
                    log.info("SOD generated Successfully");
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new TigerSignerException("Error while constracting SOD. ", e);
        } catch (SignatureException e) {
            throw new TigerSignerException("Error while constracting SOD. ", e);
        } catch (InvalidKeyException e) {
            throw new TigerSignerException("Error while constracting SOD. ", e);
        }
        if (sodData == null) {
            throw new TigerSignerException("Error while constracting SOD : Null SOD data." );
        }
        return sodData;
    }

    static byte[] getSampleSignedData(byte[] dataToBeSigned, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] encryptedDigest = null;

        Signature s = null;
        s = Signature.getInstance("SHA256withRSA");
        s.initSign(privateKey);
        s.update(dataToBeSigned);
        encryptedDigest = s.sign();
        return encryptedDigest;
    }

    private DgContents getLDSData(Map<Integer, byte[]> dgValues) {
        DgContents ldsData = new DgContents();
        //Dg1, Dg2, Dg4, Dg5, Dg7, Dg11;
        ldsData.Dg1 = dgValues.get(1);
        ldsData.Dg2 = dgValues.get(2);
        ldsData.Dg4 = dgValues.get(4);
        ldsData.Dg5 = dgValues.get(5);
        ldsData.Dg7 = dgValues.get(7);
        ldsData.Dg11 = dgValues.get(11);
        return ldsData;
    }

    private X509Certificate findIssuerCert(Collection<Certificate> chain, X509Certificate sodCert) {
        X509Certificate result = null;
        final X500Name issuer = JcaX500NameUtil.getIssuer(sodCert);
        if (log.isDebugEnabled()) {
            final StringBuilder buff = new StringBuilder();
            buff.append("Looking for ");
            buff.append(issuer);
            log.debug(buff.toString());
        }
        for (Certificate cert : chain) {
            if (cert instanceof X509Certificate) {
                final X509Certificate x509 = (X509Certificate) cert;
                final X500Name subject = JcaX500NameUtil.getSubject(x509);
                if (issuer.equals(subject)) {
                    result = (X509Certificate) cert;
                    if (log.isDebugEnabled()) {
                        log.debug("Found issuer");
                    }
                    break;
                } else {
                    if (log.isDebugEnabled()) {
                        final StringBuilder buff = new StringBuilder();
                        buff.append(issuer);
                        buff.append("!=");
                        buff.append(subject);
                        log.debug(buff.toString());
                    }
                }
            }
        }
        return result;
    }

    private void verifySignatureAndChain(final SODFile sod,
                                         final Collection<Certificate> chain) throws TigerSignerException {
        try {
            if (log.isDebugEnabled()) {
                final StringBuilder buff = new StringBuilder();
                buff.append("Verifying SOD signed by DS with issuer: ");
                buff.append(sod.toString());
                log.debug(buff.toString());
            }

            // Get Signer certificate from SOD
            final X509Certificate sodCert = sod.getDocSigningCertificate();

            // We need a Bouncy Castle certificate so reconstruct it
            final CertificateFactory factory
                    = CertificateFactory.getInstance("X.509", "BC");
            final X509Certificate signerCert
                    = (X509Certificate) factory.generateCertificate(
                    new ByteArrayInputStream(sodCert.getEncoded()));

            // Verify the SOD signature using certificate from SOD
            final boolean consistent = sod.checkDocSignature(signerCert);
            if (!consistent) {
                log.error("Failed to verify the SOD we signed ourselves.");
                log.error("Cert: " + signerCert);
                log.error("SOD: " + sod);
                throw new TigerSignerException("GeneralSecurityException : Signature not consistent");
            }

            // Find the issuer certificate from the configured chain
            final X509Certificate issuerCert = (chain == null ? null : findIssuerCert(chain, signerCert));
            if (issuerCert == null) {
                log.error("Failed to verify certificate chain");
                log.error("Cert: " + signerCert);
                log.error("SOD Cert: " + signerCert);
                log.error("Chain: " + chain);
                throw new TigerSignerException("GeneralSecurityException :Issuer of cert not in chain.");
            }

            // Verify the signer certificate using the issuer from the chain
            signerCert.verify(issuerCert.getPublicKey());
        } catch (IOException e) {
            log.error("Getting signer certificate from SOD failed", e);
            throw new TigerSignerException("GeneralSecurityException : Getting signer certificate from SOD failed", e);
        } catch (CertificateEncodingException e) {
            throw new TigerSignerException("CertificateEncodingException : ", e);
        } catch (CertificateException e) {
            throw new TigerSignerException("CertificateException : ", e);
        } catch (NoSuchAlgorithmException e) {
            throw new TigerSignerException("NoSuchAlgorithmException : ", e);
        } catch (InvalidKeyException e) {
            throw new TigerSignerException("InvalidKeyException : ", e);
        } catch (SignatureException e) {
            throw new TigerSignerException("SignatureException : ", e);
        } catch (NoSuchProviderException e) {
            throw new TigerSignerException("NoSuchProviderException : ", e);
        } catch (GeneralSecurityException e) {
            throw new TigerSignerException("GeneralSecurityException : ", e);
        }
    }

    @Override
    protected List<String> getFatalErrors() {
        final List<String> errors = super.getFatalErrors();

        errors.addAll(configErrors);
        return errors;
    }
}
