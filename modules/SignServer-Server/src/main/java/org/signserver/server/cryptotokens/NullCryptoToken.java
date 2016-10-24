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

import java.security.*;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.cesecore.util.query.QueryCriteria;
import org.signserver.common.*;
import org.signserver.server.IServices;

/**
 * Empty crypto token implementation that can be used by workers not really needing 
 * a crypto token.
 * 
 * The crypto token remain in the status given when it is constructed. Activation
 * and de-activation has no effect.
 * 
 * @author Markus Kilås
 * @version $Id: NullCryptoToken.java 5978 2015-03-27 15:34:04Z netmackan $
 */
public class NullCryptoToken extends BaseCryptoToken {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(NullCryptoToken.class);
    
    private final int status;

    public NullCryptoToken(final int status) {
        this.status = status;
    }
    
    @Override
    public void init(int workerId, Properties props) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("init");
        }
    }

    @Override
    public int getCryptoTokenStatus() {
        return status;
    }

    @Override
    public void activate(final String authenticationcode) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("activate");
        }
    }

    @Override
    public boolean deactivate() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("deactivate");
        }
        return true;
    }

    @Override
    public PrivateKey getPrivateKey(int purpose) throws CryptoTokenOfflineException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getPrivateKey(" + purpose + ")");
        }
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public PublicKey getPublicKey(int purpose) throws CryptoTokenOfflineException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getPublicKey(" + purpose + ")");
        }
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public String getProvider(int providerUsage) {
        return "BC";
    }

    @Override
    public Certificate getCertificate(int purpose) throws CryptoTokenOfflineException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getCertificate(" + purpose + ")");
        }
        return null;
    }

    @Override
    public List<Certificate> getCertificateChain(int purpose) throws CryptoTokenOfflineException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getCertificateChain(" + purpose + ")");
        }
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public ICertReqData genCertificateRequest(ISignerCertReqInfo info, boolean explicitEccParameters, boolean defaultKey) throws CryptoTokenOfflineException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("genCertificateRequest");
        }
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public boolean destroyKey(int purpose) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("destroyKey");
        }
        return true;
    }

    @Override
    public Collection<KeyTestResult> testKey(String alias, char[] authCode) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("testKey");
        }
        return Collections.emptyList();
    }

    @Override
    public KeyStore getKeyStore() throws CryptoTokenOfflineException{
        if (LOG.isDebugEnabled()) {
            LOG.debug("getKeyStore");
        }
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public int getCryptoTokenStatus(IServices services) {
        return getCryptoTokenStatus();
    }

    @Override
    public void importCertificateChain(List<Certificate> certChain, String alias, char[] athenticationCode, Map<String, Object> params, IServices services) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public TokenSearchResults searchTokenEntries(int startIndex, int max, QueryCriteria qc, boolean includeData, Map<String, Object> params, IServices services) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public ICryptoInstance acquireCryptoInstance(String alias, Map<String, Object> params, RequestContext context) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public void releaseCryptoInstance(ICryptoInstance instance, RequestContext context) {
    }

    @Override
    public void generateKey(String keyAlgorithm, String keySpec, String alias, char[] authCode, Map<String, Object> params, IServices services) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public Certificate generateSignerKeyAndGetCertificate(String keyAlgorithm, String keySpec, String alias, char[] authCode, Map<String, Object> params, IServices services) throws TokenOutOfSpaceException, CryptoTokenOfflineException, DuplicateAliasException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, UnsupportedCryptoTokenParameter {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public ICertReqData genCertificateRequest(ISignerCertReqInfo info, boolean explicitEccParameters, String keyAlias, IServices services) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public Collection<KeyTestResult> testKey(String alias, char[] authCode, IServices Services) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public PrivateKey getPrivateKey(String alias) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public PublicKey getPublicKey(String alias) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public ICertReqData genCertificateRequest(ISignerCertReqInfo info, boolean explicitEccParameters, String keyAlias) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public Certificate getCertificate(String alias) throws CryptoTokenOfflineException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getCertificate(" + alias + ")");
        }
        return null;
    }

    @Override
    public List<Certificate> getCertificateChain(String alias) throws CryptoTokenOfflineException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public void generateKey(String keyAlgorithm, String keySpec, String alias, char[] authCode) throws CryptoTokenOfflineException, IllegalArgumentException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public Certificate generateSignerKeyAndGetCertificate(String keyAlgorithm, String keySpec, String alias, char[] authCode) throws CryptoTokenOfflineException, IllegalArgumentException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }

    @Override
    public boolean removeKey(String alias) throws CryptoTokenOfflineException, KeyStoreException, SignServerException {
        throw new CryptoTokenOfflineException("Unsupported by crypto token");
    }
    
}
