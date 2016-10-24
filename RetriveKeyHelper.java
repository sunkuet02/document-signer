package org.signserver.ejb;

import iaik.pkcs.pkcs11.*;
import iaik.pkcs.pkcs11.objects.RSAPublicKey;
import iaik.pkcs.pkcs11.objects.X509PublicKeyCertificate;
import iaik.pkcs.pkcs11.wrapper.PKCS11Constants;
import iaik.pkcs.pkcs11.wrapper.PKCS11Exception;
import org.apache.log4j.Logger;
import org.signserver.common.TigerSignerException;
import org.signserver.common.WorkerConfig;
import sun.security.rsa.RSAPublicKeyImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by sun on 10/18/16.
 *
 * This Class is currently not used and It has few bugs with it's session.
 */
public class RetriveKeyHelper {
    public RetriveKeyHelper() {
    }

    private static final Logger log = Logger.getLogger(RetriveKeyHelper.class);
    public static final boolean initalizeModule = false;
    private static final String CONTENT_TYPE = "application/octet-stream";

    private static Module pkcs11Module = null;
    private static Module pkcs11ModuleRW = null;
    private static Object sessionSync = new Object();

    public PublicKey getHSMPublicKey(WorkerConfig config) throws TigerSignerException {
        String slotNo = config.getProperty("SLOTLABELVALUE");
        log.info("SlotNo : " + slotNo);
        try {
            Session session = null;
            try {
                session = getSession(slotNo, false, config);
            } catch (Exception ex) {
                session = getSession(slotNo, false, config);
            }
            String uPin = config.getProperty("PIN");
            try {
                if (session != null) {
                    log.info("session login");
                }
                session.login(Session.UserType.USER, uPin.toCharArray());
                log.info("logged in");
                return getpPublicKey(session, config);

            } catch (PKCS11Exception exc1) {

                if (exc1.getErrorCode() == PKCS11Constants.CKR_SESSION_HANDLE_INVALID) {
                    throw new TigerSignerException("CKR SESSION HANDLE INVALID: " + exc1.getErrorCode() + ", " + exc1.getMessage());
                }


                if (exc1.getErrorCode() == PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN) {
                    log.info(" already logged in");
                    return getpPublicKey(session,config);
                }

                if (exc1.getErrorCode() == PKCS11Constants.CKR_USER_NOT_LOGGED_IN) {
                    throw new TigerSignerException("CKR USER NOT LOGGED IN: " + exc1.getErrorCode() + ", " + exc1.getMessage());
                }
            } finally {
                log.info(" logged out");
            }
        } catch (Exception ex) {
            throw new TigerSignerException(ex.getMessage(), ex);
        }

        return null;

    }

    private synchronized static Session getSession(String slotId, boolean readWriteSession, WorkerConfig config) throws Exception {
        Module pkcs11Module = null;
        try {
            pkcs11Module = getModuleInstance(config);
            if (pkcs11Module != null) {
                if (initalizeModule) {
                    try {
                        pkcs11Module.finalize(null);
                    } catch (Throwable ex) {
                        log.error("couldn't finalize 1" + ex.getMessage());
                    }
                    log.info(" pkcs11 module not null ");
                    pkcs11Module.initialize(null);
                }
            } else {
                log.info(" pkcs11 module is null ");
            }
        } catch (Exception exc) {
            log.info(" module already initialized ", exc);
        }

        log.info("*****************passed");

        try {

            Slot[] slots = null;

            log.info("retrieving slots");
            slots = pkcs11Module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);

            if (slots == null || slots.length == 0) {
                throw new TokenException("No token found!");
            }

            log.info("retrieved slots");


            int slotIndex = Integer.parseInt(slotId);

            log.info("index parsed");
            if (slotIndex >= slots.length) {
                throw new TokenException("invalid slot index!");
            }

            Slot selectedSlot = slots[slotIndex];
            if (selectedSlot == null) {
                throw new TokenException("slot not found!");
            }
            Token token;
            token = selectedSlot.getToken();

            if (token == null) {
                throw new TokenException("slot token not found!");
            }

            log.info("valid slot token");
            Session session = null;
            try {
                if (readWriteSession) {
                    session = token.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RW_SESSION, null, null);
                } else {
                    session = token.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RO_SESSION, null, null);
                }
            } catch (TokenException ex) {
                log.error(ex.getMessage(), ex);
                throw new TigerSignerException(ex.getMessage(), ex);

            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
                throw new TigerSignerException(ex.getMessage(), ex);
            }

            if (session != null) {
                log.info("session retrieved");
                return session;
            }

            log.error("session null");
            throw new TigerSignerException(" couldn't retrieve session ");

        } catch (Exception ex) {
            log.error(ex.getMessage());
            log.info("error found:");
            log.error(ex);
            log.info("**** retry");
            if (pkcs11Module != null) {
                try {
                    try {
                        pkcs11Module.finalize(null);
                    } catch (Throwable t) {
                        log.error("couldn't finalize 1" + t.getMessage());
                    }
                    pkcs11Module.initialize(null);
                } catch (Exception exe) {
                    log.error("failover failed for module initialize", exe);
                }
            }
            throw new TigerSignerException(ex.getMessage(), ex);

        }
    }

    public static Module getModuleInstance(WorkerConfig config) throws IOException {
        if (config != null) {
            log.info("*** getModuleInstance : Config not null");
            if (pkcs11ModuleRW == null) {
                String lib = "";

                if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                    lib = config.getProperty("SHAREDLIBRARY", "cs_pkcs11_R2.dll");
                } else {
                    lib = config.getProperty("SHAREDLIBRARY", "libcs_pkcs11_R2.so");
                }

                log.info("*********** lib: " + lib);


                pkcs11ModuleRW = Module.getInstance(lib);
                try {
                    pkcs11ModuleRW.initialize(null);
                } catch (TokenException ex) {
                    log.info("***** module already initialized ", ex);
                }
            }
            return pkcs11ModuleRW;
        } else {
            log.info("*** getModuleInstance : Config null");
            if (pkcs11Module == null) {
                String lib = "";
                if (System.getProperty("os.name").toUpperCase().contains("WINDOWS")) {
                    lib = "cs_pkcs11_R2.dll";
                } else {
                    lib = "libcs_pkcs11_R2.so";

                }
                pkcs11Module = Module.getInstance(lib);
                try {
                    pkcs11Module.initialize(null);
                } catch (TokenException ex) {
                    log.info("***** module already initialized ", ex);
                }

            }
            return pkcs11Module;
        }

    }

    private synchronized PublicKey getpPublicKey(final Session session, WorkerConfig config) throws Exception {
        final String RETRIEVE_CERT = "Y";
        log.info("retriving public key");
//        if (config.getProperty("SIGNATUREALGORITHM").contains("RSA")) {
        {  String type = config.getProperty("RETRIEVECERT");
            if (type == null || type.isEmpty()) {
                type = RETRIEVE_CERT;
            }

            log.info("RSA signature algo **");

            String keyName = config.getProperty("DEFAULTKEY");

            log.info("default key : " + keyName);
            Object obj = getRSAPublicKey(session, keyName, type.equalsIgnoreCase(RETRIEVE_CERT));
            log.info("got rsa public key **");

            java.security.interfaces.RSAPublicKey publicKey;
            if (obj instanceof java.security.interfaces.RSAPublicKey) {
                publicKey = (java.security.interfaces.RSAPublicKey) obj;
            } else {
                RSAPublicKey pk = (RSAPublicKey) obj;
                byte[] modulusBytes = pk.getModulus().getByteArrayValue();

                byte[] publicExponentBytes = pk.getPublicExponent().getByteArrayValue();
                BigInteger modulus;
                BigInteger publicExponent;
                modulus = new BigInteger(modulusBytes);
                publicExponent = new BigInteger(publicExponentBytes);
                publicKey = new RSAPublicKeyImpl(modulus, publicExponent);
            }
            return publicKey;
        }
       // return null;
    }

    private Object getRSAPublicKey(final Session session, String keyName, boolean retrieveCert) throws Exception {


        log.info("rsa public key");

        if (retrieveCert) {
            X509PublicKeyCertificate templateCert = new X509PublicKeyCertificate();
            templateCert.getId().setByteArrayValue(keyName.getBytes());
            X509PublicKeyCertificate iaikcertificate;
            session.findObjectsInit(templateCert);
            Object[] foundSignatureKeyObjects = session.findObjects(1);
            if (foundSignatureKeyObjects != null && foundSignatureKeyObjects.length > 0) {

                iaikcertificate = (X509PublicKeyCertificate) foundSignatureKeyObjects[0];

                if (iaikcertificate != null) {
                    byte[] encoded = iaikcertificate.getValue().getByteArrayValue();

                    X509Certificate certificate = getCertificate(encoded);
                    PublicKey pk = null;
                    pk = certificate.getPublicKey();
                    return pk;
                }

            } else {
                throw new TigerSignerException("No RSA public key found that can sign!");
            }

        }

        RSAPublicKey pk = null;
        RSAPublicKey templateVerifyKey = new RSAPublicKey();
        templateVerifyKey.getVerify().setBooleanValue(Boolean.TRUE);

        session.findObjectsInit(templateVerifyKey);
        Object[] foundSignatureKeyObjects = session.findObjects(1);

        if (foundSignatureKeyObjects.length > 0) {

            pk = (RSAPublicKey) foundSignatureKeyObjects[0];
        } else {
            throw new TigerSignerException("No RSA public key found that can sign!");
        }
        session.findObjectsFinal();
        log.info(pk);
        return pk;
    }

    public static X509Certificate getCertificate(byte[] encoded) {
        X509Certificate certificate = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream bais = new ByteArrayInputStream(encoded);
            certificate = (X509Certificate) certificateFactory.generateCertificate(bais);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return certificate;
    }
}
