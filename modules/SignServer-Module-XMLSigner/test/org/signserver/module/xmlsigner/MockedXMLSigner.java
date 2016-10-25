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
package org.signserver.module.xmlsigner;

import java.security.cert.Certificate;
import java.util.List;
import org.signserver.common.CryptoTokenOfflineException;
import org.signserver.common.ProcessRequest;
import org.signserver.common.RequestContext;
import org.signserver.server.cryptotokens.ICryptoToken;
import org.signserver.test.utils.mock.MockedCryptoToken;

/**
 * Mocked version of the XMLSigner using a MockedCryptoToken.
 *
 * @author Markus Kilås
 * @version $Id: MockedXMLSigner.java 5575 2014-12-11 08:55:46Z malu9369 $
 */
public class MockedXMLSigner extends XMLSigner {
    private final MockedCryptoToken mockedToken;

    public MockedXMLSigner(final MockedCryptoToken mockedToken) {
        this.mockedToken = mockedToken;
    }

    @Override
    public Certificate getSigningCertificate(final ProcessRequest request,
                                             final RequestContext context)
            throws CryptoTokenOfflineException {
        return mockedToken.getCertificate(ICryptoToken.PURPOSE_SIGN);
    }

    @Override
    public List<Certificate> getSigningCertificateChain(final ProcessRequest request,
                                                        final RequestContext context)
            throws CryptoTokenOfflineException {
        return mockedToken.getCertificateChain(ICryptoToken.PURPOSE_SIGN);
    }

    @Override
    public ICryptoToken getCryptoToken() {
        return mockedToken;
    }

}
