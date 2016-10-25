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
package org.signserver.server.cryptotokens;

import java.security.KeyPair;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import javax.crypto.Cipher;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.ejbca.util.Base64;
import org.ejbca.util.CertTools;
import org.ejbca.util.keystore.KeyTools;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.signserver.common.*;
import org.signserver.testutils.ModulesTestCase;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.signserver.ejb.interfaces.IGlobalConfigurationSession;
import org.signserver.ejb.interfaces.IWorkerSession;

/**
 * TODO: Document me!
 * 
 * @version $Id: SoftCryptoTokenTest.java 5529 2014-12-02 13:29:21Z netmackan $
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SoftCryptoTokenTest extends ModulesTestCase {

    private final IWorkerSession workerSession = getWorkerSession();
    private final IGlobalConfigurationSession globalSession = getGlobalSession();
    
    @Before
    public void setUp() throws Exception {
        SignServerUtil.installBCProvider();
    }

    @Test
    public void test00SetupDatabase() throws Exception {
        globalSession.setProperty(GlobalConfiguration.SCOPE_GLOBAL, "WORKER88.CLASSPATH", "org.signserver.module.mrtdsigner.MRTDSigner");
        globalSession.setProperty(GlobalConfiguration.SCOPE_GLOBAL, "WORKER88.SIGNERTOKEN.CLASSPATH", "org.signserver.server.cryptotokens.SoftCryptoToken");

        workerSession.setWorkerProperty(88, "AUTHTYPE", "NOAUTH");
        workerSession.setWorkerProperty(88, "KEYALG", "RSA");
        workerSession.setWorkerProperty(88, "KEYSPEC", "2048");

        workerSession.reloadConfiguration(88);
    }

    @Test
    public void test01BasicTests() throws Exception {
        StaticWorkerStatus stat = (StaticWorkerStatus) workerSession.getStatus(88);
        assertTrue(stat.getTokenStatus() == WorkerStatus.STATUS_OFFLINE);

        PKCS10CertReqInfo crInfo = new PKCS10CertReqInfo("SHA1WithRSA", "CN=TEST1", null);
        ICertReqData reqData = workerSession.getCertificateRequest(88, crInfo, false);
        assertNotNull(reqData);
        assertTrue(reqData instanceof Base64SignerCertReqData);
        PKCS10CertificationRequest pkcs10 = new PKCS10CertificationRequest(Base64.decode(((Base64SignerCertReqData) reqData).getBase64CertReq()));
        assertTrue(pkcs10.getPublicKey() != null);

        KeyPair dummyCAKeys = KeyTools.genKeys("2048", "RSA");
        X509Certificate cert = CertTools.genSelfCert(pkcs10.getCertificationRequestInfo().getSubject().toString(), 10, null, dummyCAKeys.getPrivate(), pkcs10.getPublicKey(), "SHA1WithRSA", false);
        workerSession.uploadSignerCertificate(88, cert.getEncoded(), GlobalConfiguration.SCOPE_GLOBAL);
        workerSession.reloadConfiguration(88);

        stat = (StaticWorkerStatus) workerSession.getStatus(88);
        assertTrue(stat.getActiveSignerConfig().getProperty("KEYDATA") != null);
        assertTrue(stat.getTokenStatus() == WorkerStatus.STATUS_ACTIVE);

        int reqid = 12;
        ArrayList<byte[]> signrequests = new ArrayList<byte[]>();

        byte[] signreq1 = "Hello World".getBytes();
        byte[] signreq2 = "Hello World2".getBytes();
        signrequests.add(signreq1);
        signrequests.add(signreq2);

        MRTDSignResponse res = (MRTDSignResponse) workerSession.process(88, new MRTDSignRequest(reqid, signrequests), new RequestContext());
        assertTrue(res != null);
        assertTrue(reqid == res.getRequestID());
        Certificate signercert = res.getSignerCertificate();
        assertNotNull(signercert);

        Cipher c = Cipher.getInstance("RSA", "BC");
        c.init(Cipher.DECRYPT_MODE, signercert);

        byte[] signres1 = c.doFinal((byte[]) ((ArrayList<?>) res.getProcessedData()).get(0));

        if (!arrayEquals(signreq1, signres1)) {
            assertTrue("First MRTD doesn't match with request", false);
        }

        byte[] signres2 = c.doFinal((byte[]) ((ArrayList<?>) res.getProcessedData()).get(1));

        if (!arrayEquals(signreq2, signres2)) {
            assertTrue("Second MRTD doesn't match with request", false);
        }

        assertTrue(signercert.getPublicKey().equals(pkcs10.getPublicKey()));

        reqData = workerSession.getCertificateRequest(88, crInfo, false);
        assertNotNull(reqData);
        assertTrue(reqData instanceof Base64SignerCertReqData);
        PKCS10CertificationRequest pkcs10_2 = new PKCS10CertificationRequest(Base64.decode(((Base64SignerCertReqData) reqData).getBase64CertReq()));
        assertTrue(pkcs10_2.getPublicKey() != null);
        assertFalse(pkcs10_2.getPublicKey().equals(pkcs10.getPublicKey()));

        workerSession.deactivateSigner(88);
        stat = (StaticWorkerStatus) workerSession.getStatus(88);
        assertTrue(stat.getTokenStatus() == WorkerStatus.STATUS_OFFLINE);
        try {
            res = (MRTDSignResponse) workerSession.process(88, new MRTDSignRequest(reqid, signrequests), new RequestContext());
            assertTrue(false);
        } catch (CryptoTokenOfflineException e) {
        }

        workerSession.activateSigner(88, "anypwd");
        stat = (StaticWorkerStatus) workerSession.getStatus(88);
        assertTrue(stat.getTokenStatus() == WorkerStatus.STATUS_ACTIVE);
        res = (MRTDSignResponse) workerSession.process(88, new MRTDSignRequest(reqid, signrequests), new RequestContext());
    }

    @Test
    public void test99TearDownDatabase() throws Exception {
        removeWorker(88);
    }

    private boolean arrayEquals(byte[] signreq2, byte[] signres2) {
        boolean retval = true;

        if (signreq2.length != signres2.length) {
            return false;
        }

        for (int i = 0; i < signreq2.length; i++) {
            if (signreq2[i] != signres2[i]) {
                return false;
            }
        }
        return retval;
    }
}
