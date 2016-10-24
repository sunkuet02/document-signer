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

import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Tests that the hard token properties are set correctly for PKCS11 crypto tokens.
 *
 * @version $Id: CryptoTokenHelperTest.java 5621 2015-01-16 09:21:30Z netmackan $
 */
public class CryptoTokenHelperTest extends TestCase {
    
    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(CryptoTokenHelperTest.class);

    /**
     * Tests some slot properties, including ATTRIBUTES.
     * @throws Exception
     */
    public final void testSlotProperties1() throws Exception {
        Properties prop = new Properties();
        prop.put("SHAREDLIBRARY", "/opt/nfast/toolkits/pkcs11/libcknfast.so");
        prop.put("SLOT", "1");
        prop.put("DEFAULTKEY", "default");
        prop.put("PIN", "1234");
        prop.put("ATTRIBUTES", "my attributes");
        SortedMap p = new TreeMap(CryptoTokenHelper.fixP11Properties(prop));
        assertEquals("{ATTRIBUTES=my attributes, DEFAULTKEY=default, PIN=1234, SHAREDLIBRARY=/opt/nfast/toolkits/pkcs11/libcknfast.so, SLOT=1, SLOTLABELTYPE=SLOT_NUMBER, SLOTLABELVALUE=1, defaultKey=default, pin=1234, sharedLibrary=/opt/nfast/toolkits/pkcs11/libcknfast.so, slot=1, slotLabelType=SLOT_NUMBER, slotLabelValue=1}", p.toString());
    }

    /**
     * Tests some slot properties, including ATTRIBUTESFILE.
     * @throws Exception
     */
    public final void testSlotProperties2() throws Exception {
        Properties prop = new Properties();
        prop.put("SHAREDLIBRARY", "/opt/nfast/toolkits/pkcs11/libcknfast.so");
        prop.put("SLOT", "1");
        prop.put("DEFAULTKEY", "default");
        prop.put("PIN", "1234");
        prop.put("ATTRIBUTESFILE", "/opt/attributes.cfg");
        SortedMap p = new TreeMap(CryptoTokenHelper.fixP11Properties(prop));
        assertEquals("{ATTRIBUTESFILE=/opt/attributes.cfg, DEFAULTKEY=default, PIN=1234, SHAREDLIBRARY=/opt/nfast/toolkits/pkcs11/libcknfast.so, SLOT=1, SLOTLABELTYPE=SLOT_NUMBER, SLOTLABELVALUE=1, attributesFile=/opt/attributes.cfg, defaultKey=default, pin=1234, sharedLibrary=/opt/nfast/toolkits/pkcs11/libcknfast.so, slot=1, slotLabelType=SLOT_NUMBER, slotLabelValue=1}", p.toString());
    }

    public final void testSlotIndexProperties() throws Exception {
        // When using nCipher we have to use slotListIndex instead of slot property
        Properties prop = new Properties();
        prop.put("SHAREDLIBRARY", "/opt/nfast/toolkits/pkcs11/libcknfast.so");
        prop.put("SLOTLISTINDEX", "1");
        prop.put("DEFAULTKEY", "default");
        prop.put("PIN", "1234");
        SortedMap p = new TreeMap(CryptoTokenHelper.fixP11Properties(prop));
        assertEquals("{DEFAULTKEY=default, PIN=1234, SHAREDLIBRARY=/opt/nfast/toolkits/pkcs11/libcknfast.so, SLOTLABELTYPE=SLOT_INDEX, SLOTLABELVALUE=1, SLOTLISTINDEX=1, defaultKey=default, pin=1234, sharedLibrary=/opt/nfast/toolkits/pkcs11/libcknfast.so, slotLabelType=SLOT_INDEX, slotLabelValue=1, slotListIndex=1}", p.toString());
    }

    /**
     * Tests some slot properties, including SLOTLISTTYPE and SLITLISTVALUE.
     * @throws Exception
     */
    public final void testSlotListTypePropertiesNumber() throws Exception {
        Properties prop = new Properties();
        prop.put("SHAREDLIBRARY", "/opt/nfast/toolkits/pkcs11/libcknfast.so");
        prop.put("SLOTLABELTYPE", "SLOT_NUMBER");
        prop.put("SLOTLABELVALUE", "1");
        prop.put("DEFAULTKEY", "default");
        prop.put("PIN", "1234");
        prop.put("ATTRIBUTESFILE", "/opt/attributes.cfg");
        SortedMap p = new TreeMap(CryptoTokenHelper.fixP11Properties(prop));
        assertEquals("{ATTRIBUTESFILE=/opt/attributes.cfg, DEFAULTKEY=default, PIN=1234, SHAREDLIBRARY=/opt/nfast/toolkits/pkcs11/libcknfast.so, SLOTLABELTYPE=SLOT_NUMBER, SLOTLABELVALUE=1, attributesFile=/opt/attributes.cfg, defaultKey=default, pin=1234, sharedLibrary=/opt/nfast/toolkits/pkcs11/libcknfast.so, slotLabelType=SLOT_NUMBER, slotLabelValue=1}", p.toString());
    }

    /**
     * Tests some slot properties, including SLOTLISTTYPE and SLITLISTVALUE.
     * @throws Exception
     */
    public final void testSlotListTypePropertiesIndex() throws Exception {
        Properties prop = new Properties();
        prop.put("SHAREDLIBRARY", "/opt/nfast/toolkits/pkcs11/libcknfast.so");
        prop.put("SLOTLABELTYPE", "SLOT_INDEX");
        prop.put("SLOTLABELVALUE", "1");
        prop.put("DEFAULTKEY", "default");
        prop.put("PIN", "1234");
        prop.put("ATTRIBUTESFILE", "/opt/attributes.cfg");
        SortedMap p = new TreeMap(CryptoTokenHelper.fixP11Properties(prop));
        assertEquals("{ATTRIBUTESFILE=/opt/attributes.cfg, DEFAULTKEY=default, PIN=1234, SHAREDLIBRARY=/opt/nfast/toolkits/pkcs11/libcknfast.so, SLOTLABELTYPE=SLOT_INDEX, SLOTLABELVALUE=1, attributesFile=/opt/attributes.cfg, defaultKey=default, pin=1234, sharedLibrary=/opt/nfast/toolkits/pkcs11/libcknfast.so, slotLabelType=SLOT_INDEX, slotLabelValue=1}", p.toString());
    }

    /**
     * Tests some slot properties, including SLOTLISTTYPE and SLITLISTVALUE.
     * @throws Exception
     */
    public final void testSlotListTypePropertiesLabel() throws Exception {
        Properties prop = new Properties();
        prop.put("SHAREDLIBRARY", "/opt/nfast/toolkits/pkcs11/libcknfast.so");
        prop.put("SLOTLABELTYPE", "SLOT_LABEL");
        prop.put("SLOTLABELVALUE", "MyLabel");
        prop.put("DEFAULTKEY", "default");
        prop.put("PIN", "1234");
        prop.put("ATTRIBUTESFILE", "/opt/attributes.cfg");
        SortedMap p = new TreeMap(CryptoTokenHelper.fixP11Properties(prop));
        assertEquals("{ATTRIBUTESFILE=/opt/attributes.cfg, DEFAULTKEY=default, PIN=1234, SHAREDLIBRARY=/opt/nfast/toolkits/pkcs11/libcknfast.so, SLOTLABELTYPE=SLOT_LABEL, SLOTLABELVALUE=MyLabel, attributesFile=/opt/attributes.cfg, defaultKey=default, pin=1234, sharedLibrary=/opt/nfast/toolkits/pkcs11/libcknfast.so, slotLabelType=SLOT_LABEL, slotLabelValue=MyLabel}", p.toString());
    }
    
    // TODO: Tests for dummy certificates temporarly moved to
    // SignServer-Test-System but can be moved back after upgrading to
    // BC >= 1.50

}
