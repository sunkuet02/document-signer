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

import org.signserver.common.CryptoTokenOfflineException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;

/**
 * Interface that should be implmented by CryptoTokenS supporting key
 * generation.
 *
 * @author Markus Kilås
 * @version $Id: IKeyGenerator.java 2345 2012-05-06 09:03:57Z netmackan $
 */
public interface IKeyGenerator {

    byte[] decryptByteData(String alias, String pin, byte[] encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, CryptoTokenOfflineException, UnrecoverableKeyException, KeyStoreException, InvalidKeyException;

    /**
     * Generate a new keypair.
     * @param keyAlgorithm Key algorithm
     * @param keySpec Key specification
     * @param alias Name of the new key
     * @param authCode Authorization code
     * @throws CryptoTokenOfflineException
     * @throws IllegalArgumentException
     */
    void generateKey(String keyAlgorithm, String keySpec, String alias,
                     char[] authCode) throws CryptoTokenOfflineException,
            IllegalArgumentException;

    Certificate generateSignerKeyAndGetCertificate(String keyAlgorithm, String keySpec, String alias,
                                                   char[] authCode) throws CryptoTokenOfflineException,
            IllegalArgumentException, KeyStoreException;
}