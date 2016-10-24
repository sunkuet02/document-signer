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
package org.signserver.server.aliasselectors;

import junit.framework.TestCase;
import org.signserver.common.GenericSignRequest;
import org.signserver.common.RequestContext;
import org.signserver.common.WorkerConfig;
import org.signserver.server.cryptotokens.ICryptoToken;

/**
 * Unit test for the default alias selector.
 * 
 * @author Marcus Lundblad
 * @version $Id: DefaultAliasSelectorUnitTest.java 5557 2014-12-08 13:49:26Z malu9369 $
 */
public class DefaultAliasSelectorUnitTest extends TestCase {
    
    /**
     * Test that setting initializing a DefaultAliasSelector instance with
     * a worker config containing a DEFAULTKEY.
     * 
     * @throws Exception 
     */
    public void testDefaultAlias() throws Exception {
       final WorkerConfig config = new WorkerConfig();
       
       config.setProperty("DEFAULTKEY", "defaultkey");
       config.setProperty("NEXTCERTSIGNKEY", "nextkey");
       
       final AliasSelector selector = new DefaultAliasSelector();
       
       selector.init(4711, config, null, null);
       assertEquals("default alias", "defaultkey",
               selector.getAlias(ICryptoToken.PURPOSE_SIGN, null,
                                 new GenericSignRequest(4711, new byte[1]),
                                 new RequestContext()));
    }
    
    /**
     * Test that setting initializing a DefaultAliasSelector instance with
     * a worker config containing a NEXTCERTSIGNKEY.
     * 
     * @throws Exception 
     */
    public void testNextKeyAlias() throws Exception {
       final WorkerConfig config = new WorkerConfig();
       
       config.setProperty("DEFAULTKEY", "defaultkey");
       config.setProperty("NEXTCERTSIGNKEY", "nextkey");
       
       final AliasSelector selector = new DefaultAliasSelector();
       
       selector.init(4711, config, null, null);
       assertEquals("next key alias", "nextkey",
               selector.getAlias(ICryptoToken.PURPOSE_NEXTKEY, null,
                                 new GenericSignRequest(4711, new byte[1]),
                                 new RequestContext()));
    }
}
