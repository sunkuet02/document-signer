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
package org.signserver.server.dispatchers;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.ejbca.util.CertTools;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.signserver.common.*;
import org.signserver.ejb.interfaces.IWorkerSession;
import org.signserver.testutils.ModulesTestCase;
import org.junit.Before;
import org.junit.Test;
import org.signserver.test.utils.builders.CertBuilder;
import org.signserver.test.utils.builders.CryptoUtils;

/**
 * Tests for the FirstActiveDispatcher.
 *
 * @author Markus Kilås
 * @version $Id: FirstActiveDispatcherTest.java 5728 2015-02-18 13:47:32Z netmackan $
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirstActiveDispatcherTest extends ModulesTestCase {

    /** Logger for this class */
    private static final Logger LOG = Logger.getLogger(FirstActiveDispatcherTest.class);

    private static final int WORKERID_DISPATCHER = 5680;
    private static final int WORKERID_1 = 5681;
    private static final String WORKERNAME_1 = "TestXMLSigner81";
    private static final int WORKERID_2 = 5682;
    private static final String WORKERNAME_2 = "TestXMLSigner82";
    private static final int WORKERID_3 = 5683;
    private static final String WORKERNAME_3 = "TestXMLSigner83";
    
    private static final int[] WORKERS = new int[] {WORKERID_DISPATCHER, WORKERID_1, WORKERID_2, WORKERID_3};
    
    /**
     * Dummy authentication code used to test activation of a dispatcher worker
     */
    private static final String DUMMY_AUTH_CODE = "1234";
    
    private final IWorkerSession workerSession = getWorkerSession();
    
    @Before
    public void setUp() throws Exception {
        SignServerUtil.installBCProvider();
    }

    @Test
    public void test00SetupDatabase() throws Exception {
        Properties conf = new Properties();
        conf.setProperty("GLOB.WORKER" + WORKERID_DISPATCHER + ".CLASSPATH", "org.signserver.server.dispatchers.FirstActiveDispatcher");
        conf.setProperty("WORKER" + WORKERID_DISPATCHER + ".NAME", "FirstActiveDispatcher80");
        conf.setProperty("WORKER" + WORKERID_DISPATCHER + ".AUTHTYPE", "NOAUTH");
        conf.setProperty("WORKER" + WORKERID_DISPATCHER + ".WORKERS", "TestXMLSigner81,TestXMLSigner82, TestXMLSigner83");
        setProperties(conf);
        workerSession.reloadConfiguration(WORKERID_DISPATCHER);
        
        KeyPair issuerKeyPair = CryptoUtils.generateRSA(512);

        // Setup signers with different certificates
        addDummySigner(WORKERID_1, WORKERNAME_1, true);
        addCertificate(issuerKeyPair.getPrivate(), WORKERID_1, WORKERNAME_1);
        addDummySigner(WORKERID_2, "TestXMLSigner82", true);
        addCertificate(issuerKeyPair.getPrivate(), WORKERID_2, WORKERNAME_2);
        addDummySigner(WORKERID_3, "TestXMLSigner83", true);
        addCertificate(issuerKeyPair.getPrivate(), WORKERID_3, WORKERNAME_3);
    }
    
    /**
     * Sets the DispatchedAuthorizer for the dispatchees.
     */
    private void setDispatchedAuthorizerForAllWorkers() {
        workerSession.setWorkerProperty(WORKERID_1, "AUTHTYPE", "org.signserver.server.DispatchedAuthorizer");
        workerSession.setWorkerProperty(WORKERID_1, "AUTHORIZEALLDISPATCHERS", "true");
        workerSession.setWorkerProperty(WORKERID_2, "AUTHTYPE", "org.signserver.server.DispatchedAuthorizer");
        workerSession.setWorkerProperty(WORKERID_2, "AUTHORIZEALLDISPATCHERS", "true");
        workerSession.setWorkerProperty(WORKERID_3, "AUTHTYPE", "org.signserver.server.DispatchedAuthorizer");
        workerSession.setWorkerProperty(WORKERID_3, "AUTHORIZEALLDISPATCHERS", "true");
        workerSession.reloadConfiguration(WORKERID_1);
        workerSession.reloadConfiguration(WORKERID_2);
        workerSession.reloadConfiguration(WORKERID_3);
    }
    
    /**
     * Resets authorization for the dispatchees to be able to call them directly.
     */
    private void resetDispatchedAuthorizerForAllWorkers() {
        workerSession.setWorkerProperty(WORKERID_1, "AUTHTYPE", "NOAUTH");
        workerSession.removeWorkerProperty(WORKERID_1, "AUTHORIZEALLDISPATCHERS");
        workerSession.setWorkerProperty(WORKERID_2, "AUTHTYPE", "NOAUTH");
        workerSession.removeWorkerProperty(WORKERID_2, "AUTHORIZEALLDISPATCHERS");
        workerSession.setWorkerProperty(WORKERID_3, "AUTHTYPE", "NOAUTH");
        workerSession.removeWorkerProperty(WORKERID_3, "AUTHORIZEALLDISPATCHERS");
        workerSession.reloadConfiguration(WORKERID_1);
        workerSession.reloadConfiguration(WORKERID_2);
        workerSession.reloadConfiguration(WORKERID_3);
    }
    

    /**
     * Tests that requests sent to the dispatching worker are forwarded to
     * any of the configured workers.
     * @throws Exception in case of exception
     */
    @Test
    public void test01Dispatched() throws Exception {
        LOG.info("test01Dispatched");
        try {
            final RequestContext context = new RequestContext();
    
            final GenericSignRequest request =
                    new GenericSignRequest(1, "<root/>".getBytes());
    
            GenericSignResponse res;
    
            setDispatchedAuthorizerForAllWorkers();
            
            // Send request to dispatcher
            res = (GenericSignResponse) workerSession.process(WORKERID_DISPATCHER,
                    request, context);
            
            X509Certificate cert = (X509Certificate) res.getSignerCertificate();
            assertTrue("Response from signer 81, 82 or 83",
                cert.getSubjectDN().getName().contains(WORKERNAME_1)
                || cert.getSubjectDN().getName().contains(WORKERNAME_2)
                || cert.getSubjectDN().getName().contains(WORKERNAME_3));
    
            // Disable signer 81
            workerSession.setWorkerProperty(WORKERID_1, "DISABLED", "TRUE");
            workerSession.reloadConfiguration(WORKERID_1);
    
            // Send request to dispatcher
            res = (GenericSignResponse) workerSession.process(WORKERID_DISPATCHER,
                    request, context);
    
            cert = (X509Certificate) res.getSignerCertificate();
            assertTrue("Response from signer 82 or 83",
                cert.getSubjectDN().getName().contains(WORKERNAME_2)
                || cert.getSubjectDN().getName().contains(WORKERNAME_3));
    
            // Disable signer 83
            workerSession.setWorkerProperty(WORKERID_3, "DISABLED", "TRUE");
            workerSession.reloadConfiguration(WORKERID_3);
    
            // Send request to dispatcher
            res = (GenericSignResponse) workerSession.process(WORKERID_DISPATCHER,
                    request, context);
    
            cert = (X509Certificate) res.getSignerCertificate();
            assertTrue("Response from signer 82",
                cert.getSubjectDN().getName().contains(WORKERNAME_2));
    
            // Disable signer 82
            workerSession.setWorkerProperty(WORKERID_2, "DISABLED", "TRUE");
            workerSession.reloadConfiguration(WORKERID_2);
    
            // Send request to dispatcher
            try {
                workerSession.process(WORKERID_DISPATCHER, request, context);
                fail("Should have got CryptoTokenOfflineException");
            } catch(CryptoTokenOfflineException ex) {
                // OK
            }
    
            // Enable signer 81
            workerSession.setWorkerProperty(WORKERID_1, "DISABLED", "FALSE");
            workerSession.reloadConfiguration(WORKERID_1);
    
            // Send request to dispatcher
            res = (GenericSignResponse) workerSession.process(WORKERID_DISPATCHER,
                    request, context);
    
            cert = (X509Certificate) res.getSignerCertificate();
            assertTrue("Response from signer 81",
                cert.getSubjectDN().getName().contains(WORKERNAME_1));
        } finally {
            resetDispatchedAuthorizerForAllWorkers();
        }
    }
    
    /**
     * Test that trying to activate the dispatcher worker doesn't throw an exception (DSS-380)
     * This will actually not activate any crypto token
     * 
     * @throws Exception
     */
    @Test
    public void test02Activate() throws Exception {
        LOG.info("test02Activate");
    	try {
    		workerSession.activateSigner(WORKERID_DISPATCHER, DUMMY_AUTH_CODE);
    	} catch (Exception e) {
    		LOG.error("Exception thrown", e);
    		fail("Failed to activate the dispatcher");
    	}
    }

    /**
     * Test that trying to deactivate the dispatcher doesn't throw an exception (DSS-380)
     * @throws Exception
     */
    @Test
    public void test03Deactivate() throws Exception {
        LOG.info("test03Deactivate");
    	try {
    		workerSession.deactivateSigner(WORKERID_DISPATCHER);
    	} catch (Exception e) {
    		LOG.error("Exception thrown", e);
    		fail("Failed to deactive the dispatcher");
    	}
    }

    @Test
    public void test99TearDownDatabase() throws Exception {
        LOG.info("test99TearDownDatabase");
        removeWorker(WORKERID_DISPATCHER);
        for (int workerId : WORKERS) {
            removeWorker(workerId);
        }
    }

    private void addCertificate(PrivateKey issuerPrivateKey, int workerId, String workerName) throws CryptoTokenOfflineException, InvalidWorkerIdException, IOException, CertificateException, OperatorCreationException {
        Base64SignerCertReqData reqData = (Base64SignerCertReqData) workerSession.getCertificateRequest(workerId, new PKCS10CertReqInfo("SHA1withRSA", "CN=" + workerName, null), false);
        PKCS10CertificationRequest csr = new PKCS10CertificationRequest(Base64.decode(reqData.getBase64CertReq()));
        X509CertificateHolder cert = new X509v3CertificateBuilder(new X500Name("CN=Issuer"), BigInteger.ONE, new Date(), new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(365)), csr.getSubject(), csr.getSubjectPublicKeyInfo()).build(new JcaContentSignerBuilder("SHA256WithRSA").setProvider("BC").build(issuerPrivateKey));
        workerSession.setWorkerProperty(workerId, "SIGNERCERTCHAIN", new String(CertTools.getPEMFromCerts(Arrays.asList(new JcaX509CertificateConverter().getCertificate(cert)))));
        workerSession.reloadConfiguration(workerId);
    }

}