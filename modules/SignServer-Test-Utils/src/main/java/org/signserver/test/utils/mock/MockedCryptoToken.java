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
package org.signserver.test.utils.mock;

import java.security.*;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.cesecore.util.query.QueryCriteria;
import org.signserver.common.*;
import org.signserver.server.IServices;
import org.signserver.server.cryptotokens.DefaultCryptoInstance;
import org.signserver.server.cryptotokens.ICryptoInstance;
import org.signserver.server.cryptotokens.ICryptoToken;
import org.signserver.server.cryptotokens.ICryptoTokenV3;
import org.signserver.server.cryptotokens.TokenSearchResults;

/**
 * CryptoToken backed by the provided Keys and Certificates.
 * Only used methods are implemented.
 * 
 * @author Markus Kilås
 * @version $Id: MockedCryptoToken.java 5978 2015-03-27 15:34:04Z netmackan $
 */
public class MockedCryptoToken implements ICryptoToken, ICryptoTokenV3 {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(MockedCryptoToken.class);
    
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Certificate signerCertificate;
    private List<Certificate> certificateChain;
    private String provider;

    private int privateKeyCalls;
    
    public MockedCryptoToken(PrivateKey privateKey, PublicKey publicKey, Certificate signerCertificate, List<Certificate> certificateChain, String provider) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.signerCertificate = signerCertificate;
        this.certificateChain = certificateChain;
        this.provider = provider;
    }
    
    public void init(int workerId, Properties props) throws CryptoTokenInitializationFailureException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCryptoTokenStatus() {
        LOG.debug(">getCryptoTokenStatus");
        return WorkerStatus.STATUS_ACTIVE;
    }
    
    public int getCryptoTokenStatus(final IServices services) {
        return getCryptoTokenStatus();
    }

    public void activate(String authenticationcode) throws CryptoTokenAuthenticationFailureException, CryptoTokenOfflineException {
        LOG.debug(">activate");
    }

    public boolean deactivate() throws CryptoTokenOfflineException {
        LOG.debug(">deactivate");
        return true;
    }

    public PrivateKey getPrivateKey(int purpose) throws CryptoTokenOfflineException {
        LOG.debug(">getPrivateKey");
        privateKeyCalls++;
        return privateKey;
    }

    public PublicKey getPublicKey(int purpose) throws CryptoTokenOfflineException {
        LOG.debug(">getPublicKey");
        return publicKey;
    }

    public String getProvider(int providerUsage) {
        LOG.debug(">getProvider");
        return provider;
    }

    public Certificate getCertificate(int purpose) throws CryptoTokenOfflineException {
        LOG.debug(">getCertificate");
        return signerCertificate;
    }

    public List<Certificate> getCertificateChain(int purpose) throws CryptoTokenOfflineException {
        LOG.debug(">getCertificateChain");
        return certificateChain;
    }

    public ICertReqData genCertificateRequest(ISignerCertReqInfo info, boolean explicitEccParameters, boolean defaultKey) throws CryptoTokenOfflineException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean destroyKey(int purpose) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<KeyTestResult> testKey(String alias, char[] authCode) throws CryptoTokenOfflineException, KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public KeyStore getKeyStore() throws UnsupportedOperationException, CryptoTokenOfflineException, KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPrivateKeyCalls() {
        return privateKeyCalls;
    }
    
    @Override
    public void importCertificateChain(List<Certificate> certChain, String alias, char[] athenticationCode, Map<String, Object> params, IServices services) throws CryptoTokenOfflineException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TokenSearchResults searchTokenEntries(int startIndex, int max, QueryCriteria qc, boolean includeData, Map<String, Object> params, IServices services) throws CryptoTokenOfflineException, QueryException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ICryptoInstance acquireCryptoInstance(String alias, Map<String, Object> params, RequestContext context) throws CryptoTokenOfflineException, NoSuchAliasException, InvalidAlgorithmParameterException, UnsupportedCryptoTokenParameter, IllegalRequestException {
        return new DefaultCryptoInstance(alias, context, Security.getProvider(provider), privateKey, certificateChain);
    }

    @Override
    public void releaseCryptoInstance(ICryptoInstance instance, RequestContext context) {
        // NOP
    }

    @Override
    public PrivateKey getPrivateKey(String alias) throws CryptoTokenOfflineException {
        return privateKey;
    }

    @Override
    public PublicKey getPublicKey(String alias) throws CryptoTokenOfflineException {
        return signerCertificate.getPublicKey();
    }

    @Override
    public ICertReqData genCertificateRequest(ISignerCertReqInfo info, boolean explicitEccParameters, String keyAlias) throws CryptoTokenOfflineException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Certificate getCertificate(String alias) throws CryptoTokenOfflineException {
        return signerCertificate;
    }

    @Override
    public List<Certificate> getCertificateChain(String alias) throws CryptoTokenOfflineException {
        return certificateChain;
    }

    @Override
    public void generateKey(String keyAlgorithm, String keySpec, String alias, char[] authCode) throws CryptoTokenOfflineException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Certificate generateSignerKeyAndGetCertificate(String keyAlgorithm, String keySpec, String alias, char[] authCode) throws CryptoTokenOfflineException, IllegalArgumentException, KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeKey(String alias) throws CryptoTokenOfflineException, KeyStoreException, SignServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generateKey(String keyAlgorithm, String keySpec, String alias, char[] authCode, Map<String, Object> params, IServices services) throws CryptoTokenOfflineException, IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Certificate generateSignerKeyAndGetCertificate(String keyAlgorithm, String keySpec, String alias, char[] authCode, Map<String, Object> params, IServices services) throws TokenOutOfSpaceException, CryptoTokenOfflineException, DuplicateAliasException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnsupportedCryptoTokenParameter {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ICertReqData genCertificateRequest(ISignerCertReqInfo info, boolean explicitEccParameters, String keyAlias, IServices services) throws CryptoTokenOfflineException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<KeyTestResult> testKey(String alias, char[] authCode, IServices Services) throws CryptoTokenOfflineException, KeyStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
