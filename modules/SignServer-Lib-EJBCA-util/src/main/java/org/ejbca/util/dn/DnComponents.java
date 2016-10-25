/*************************************************************************
 *                                                                       *
 *  EJBCA: The OpenSource Certificate Authority                          *
 *                                                                       *
 *  This software is free software; you can redistribute it and/or       *
 *  modify it under the terms of the GNU Lesser General Public           *
 *  License as published by the Free Software Foundation; either         *
 *  version 2.1 of the License, or any later version.                    *
 *                                                                       *
 *  See terms of license at gnu.org.                                     *
 *                                                                       *
 *************************************************************************/

package org.ejbca.util.dn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.X509Name;

/** Class holding information and utilitites for handling different DN components, CN, O etc
 * 
 * This is a very complex class with lots of maps and stuff. It is because it is a first step of refactoring the DN/AltName/DirAttr handling. 
 * This previously consisted of lotsa of different arrays spread out all over the place, now it's gathered here in order to be able to get a view of it.
 * The underlying implementations have not changed much though, in order to still have things working, therefore there are lots of different maps and arrays, with
 * seemingly similar contents. 
 * 
 * @author tomas
 * @version $Id: DnComponents.java 7305 2009-04-22 11:35:48Z anatom $
 */
public class DnComponents {
    private static Logger log = Logger.getLogger(DnComponents.class);

    /** This class should be instantiated immediately */
    private static DnComponents obj = new DnComponents();
    
    /** BC X509Name contains some lookup tables that could maybe be used here. 
     * 
     * This map is used in CertTools so sort and order DN strings so they all look the same in the database.
     * */
    private static HashMap<String, ASN1ObjectIdentifier> oids = new HashMap<String, ASN1ObjectIdentifier>();
    // Default values
    static {
        oids.put("c", X509Name.C);
        oids.put("dc", X509Name.DC);
        oids.put("st", X509Name.ST);
        oids.put("l", X509Name.L);
        oids.put("o", X509Name.O);
        oids.put("ou", X509Name.OU);
        oids.put("t", X509Name.T);
        oids.put("surname", X509Name.SURNAME);
        oids.put("initials", X509Name.INITIALS);
        oids.put("givenname", X509Name.GIVENNAME);
        oids.put("gn", X509Name.GIVENNAME);
        oids.put("sn", X509Name.SN);
        oids.put("serialnumber", X509Name.SN);
        oids.put("cn", X509Name.CN);
        oids.put("uid", X509Name.UID);
        oids.put("dn", X509Name.DN_QUALIFIER);
        oids.put("emailaddress", X509Name.EmailAddress);
        oids.put("e", X509Name.EmailAddress);
        oids.put("email", X509Name.EmailAddress);
        oids.put("unstructuredname", X509Name.UnstructuredName); //unstructuredName 
        oids.put("unstructuredaddress", X509Name.UnstructuredAddress); //unstructuredAddress
        oids.put("postalcode", X509Name.POSTAL_CODE);
        oids.put("businesscategory", X509Name.BUSINESS_CATEGORY);
        oids.put("postaladdress", X509Name.POSTAL_ADDRESS);
        oids.put("telephonenumber", X509Name.TELEPHONE_NUMBER);
        oids.put("pseudonym", X509Name.PSEUDONYM);
        oids.put("street", X509Name.STREET);
        oids.put("name", X509Name.NAME);
        
    }
    /** Default values used when constructing DN strings that are put in the database
     * 
     */
    private static String[] dNObjectsForward = {
        "street", "pseudonym", "telephonenumber", "postaladdress", "businesscategory", "postalcode", "unstructuredaddress", "unstructuredname", "emailaddress", "e", "email", "dn", "uid", "cn", "name", "sn", "serialnumber", "gn", "givenname",
        "initials", "surname", "t", "ou", "o", "l", "st", "dc", "c"
    };
    // Default values    
    private static String[] dNObjectsReverse = null;

    /**
     * These maps and constants are used in the admin-GUI and in End Entity profiles
     */

    /** These constants can be used when referring to standard, build in components 
     * 
     */
    // DN components
    public static final String DNEMAIL             = "DNEMAIL";
    public static final String DNQUALIFIER         = "DN";
    public static final String UID                 = "UID";
    public static final String COMMONNAME          = "COMMONNAME";
    public static final String SN                  = "SN";
    public static final String GIVENNAME           = "GIVENNAME";
    public static final String INITIALS            = "INITIALS";
    public static final String SURNAME             = "SURNAME";
    public static final String TITLE               = "TITLE";
    public static final String ORGANIZATIONUNIT    = "ORGANIZATIONUNIT";
    public static final String ORGANIZATION        = "ORGANIZATION";
    public static final String LOCALE              = "LOCALE";
    public static final String STATE               = "STATE";
    public static final String DOMAINCOMPONENT     = "DOMAINCOMPONENT";
    public static final String COUNTRY             = "COUNTRY";
    public static final String UNSTRUCTUREDADDRESS = "UNSTRUCTUREDADDRESS";
    public static final String UNSTRUCTUREDNAME    = "UNSTRUCTUREDNAME";
    public static final String POSTALCODE          = "POSTALCODE";
    public static final String BUSINESSCATEGORY    = "BUSINESSCATEGORY";
    public static final String POSTALADDRESS       = "POSTALADDRESS";
    public static final String TELEPHONENUMBER     = "TELEPHONENUMBER";
    public static final String PSEUDONYM           = "PSEUDONYM";
    public static final String STREET              = "STREET";
    public static final String NAME                = "NAME";
    
    // AltNames
    public static final String RFC822NAME         = "RFC822NAME";
    public static final String DNSNAME            = "DNSNAME";
    public static final String IPADDRESS          = "IPADDRESS";
    public static final String UNIFORMRESOURCEID  = "UNIFORMRESOURCEID";
    public static final String DIRECTORYNAME      = "DIRECTORYNAME";
    public static final String UPN                = "UPN";
    public static final String GUID               = "GUID";
    public static final String KRB5PRINCIPAL      = "KRB5PRINCIPAL";
    // Below are altNames that are not implemented yet
    public static final String OTHERNAME          = "OTHERNAME";
    public static final String X400ADDRESS        = "X400ADDRESS";
    public static final String EDIPARTNAME        = "EDIPARTNAME";
    public static final String REGISTEREDID       = "REGISTEREDID";
    
    // Subject directory attributes
    public static final String DATEOFBIRTH         = "DATEOFBIRTH";
    public static final String PLACEOFBIRTH        = "PLACEOFBIRTH";
    public static final String GENDER              = "GENDER";
    public static final String COUNTRYOFCITIZENSHIP = "COUNTRYOFCITIZENSHIP";
    public static final String COUNTRYOFRESIDENCE  = "COUNTRYOFRESIDENCE";

    private static HashMap dnNameIdMap = new HashMap();
    private static HashMap profileNameIdMap = new HashMap();
    private static HashMap dnIdToProfileNameMap = new HashMap();
    private static HashMap dnIdToProfileIdMap = new HashMap();
    private static HashMap profileIdToDnIdMap = new HashMap();
    private static HashMap dnErrorTextMap = new HashMap();
    private static HashMap profileNameLanguageMap = new HashMap();
    private static HashMap profileIdLanguageMap = new HashMap();
    private static HashMap dnIdErrorMap = new HashMap();
    private static HashMap dnIdToExtractorFieldMap = new HashMap();
    private static HashMap altNameIdToExtractorFieldMap = new HashMap();
    private static HashMap dirAttrIdToExtractorFieldMap = new HashMap();
    private static ArrayList dnProfileFields = new ArrayList();
    private static ArrayList dnLanguageTexts = new ArrayList();
    private static ArrayList dnDnIds = new ArrayList();
    private static ArrayList altNameFields = new ArrayList();
    private static ArrayList altNameLanguageTexts = new ArrayList();
    private static ArrayList altNameDnIds = new ArrayList();
    private static ArrayList dirAttrFields = new ArrayList();
    private static ArrayList dirAttrLanguageTexts = new ArrayList();
    private static ArrayList dirAttrDnIds = new ArrayList();
    private static ArrayList dnExtractorFields = new ArrayList();
    private static ArrayList altNameExtractorFields = new ArrayList();
    private static ArrayList dirAttrExtractorFields = new ArrayList();
    

    // Load values from a properties file, if it exists
    static {
        DnComponents.load();
    }
    /** This property is true if reverse DN order should be used. Default value is false (forward order).
     * This setting is changed from ejbca.properties, but should NOT be used, since there
     * is nowadays a configuration setting for the CA instead.
     * @deprecated use soft configuration on CA instead
     */
    private static final boolean reverseOrder = BooleanUtils.toBoolean("@certtools.dnorderreverse@");
    
    public static boolean isReverseOrder() {
    	return reverseOrder == true;
    }
    
    public static ASN1ObjectIdentifier getOid(String o) {
        return oids.get(o.toLowerCase());
    } // getOid

    public static ArrayList getDnProfileFields() {
    	return dnProfileFields;
    }
    public static ArrayList getDnLanguageTexts() {
    	return dnLanguageTexts;
    }
    public static ArrayList getAltNameFields() {
    	return altNameFields;
    }
    public static ArrayList getAltNameLanguageTexts() {
    	return altNameLanguageTexts;
    }
    public static ArrayList getDirAttrFields() {
    	return dirAttrFields;
    }
    // Used by DNFieldExtractor and EntityProfile, don't USE
    public static ArrayList getDirAttrDnIds() {
    	return dirAttrDnIds;
    }
    // Used by DNFieldExtractor and EntityProfile, don't USE
    public static ArrayList getAltNameDnIds() {
    	return altNameDnIds;
    }
    // Used by DNFieldExtractor and EntityProfile, don't USE
    public static ArrayList getDnDnIds() {
    	return dnDnIds;
    }
    // Used only by DNFieldExtractor, don't USE
    protected static ArrayList getDnExtractorFields() {
    	return dnExtractorFields;
    }
    protected static String getDnExtractorFieldFromDnId(int field) {
    	String val = (String)dnIdToExtractorFieldMap.get(new Integer(field));
    	return val;    	
    }
    // Used only by DNFieldExtractor, don't USE
    protected static ArrayList getAltNameExtractorFields() {
    	return altNameExtractorFields;
    }
    protected static String getAltNameExtractorFieldFromDnId(int field) {
    	String val = (String)altNameIdToExtractorFieldMap.get(new Integer(field));
    	return val;    	
    }
    // Used only by DNFieldExtractor, don't USE
    protected static ArrayList getDirAttrExtractorFields() {
    	return dirAttrExtractorFields;
    }
    protected static String getDirAttrExtractorFieldFromDnId(int field) {
    	String val = (String)dirAttrIdToExtractorFieldMap.get(new Integer(field));
    	return val;    	
    }
    
    public static String dnIdToProfileName(int dnid) {
    	String val = (String)dnIdToProfileNameMap.get(new Integer(dnid));
    	return val;
    }
    public static int dnIdToProfileId(int dnid) {
    	Integer val = (Integer)dnIdToProfileIdMap.get(new Integer(dnid));
    	return val.intValue();
    }
    /**
     * Method to get a language error constant for the admin-GUI from a profile name
     */
    public static String getLanguageConstantFromProfileName(String name) {
    	String ret = (String)profileNameLanguageMap.get(name);
    	return ret;
    }
    /**
     * Method to get a language error constant for the admin-GUI from a profile id
     */
    public static String getLanguageConstantFromProfileId(int id) {
    	String ret = (String)profileIdLanguageMap.get(new Integer(id));
    	return ret;
    }
    /**
     * Method to get a clear text error msg for the admin-GUI from a dn id
     */
    public static String getErrTextFromDnId(int id) {
    	String ret = (String)dnIdErrorMap.get(new Integer(id));
    	return ret;
    }
    
    
    /** This method is only used to initialize EndEntityProfile, because of legacy baggage.
     * Should be refactored sometime! Please don't use this whatever you do!
     * @return
     */
    public static HashMap getProfilenameIdMap() {
    	return profileNameIdMap;
    	
    }
    /** A function that takes an fieldId pointing to a corresponding id in UserView and DnFieldExctractor.
     *  For example : profileFieldIdToUserFieldIdMapper(EndEntityProfile.COMMONNAME) returns DnFieldExctractor.COMMONNAME.
     *
     *  Should only be used with subjectDN, Subject Alternative Names and subject directory attribute fields.
     */
    public static int profileIdToDnId(int profileid) {
    	Integer val = (Integer)profileIdToDnIdMap.get(new Integer(profileid));
    	if (val == null) {
    		log.error("No dn id mapping from profile id "+profileid);
    		// We allow it to fail here
    	}
    	return val.intValue();
    }
    /**
     * Returns the dnObjects (forward or reverse) that is in use
     */
    public static String[]getDnObjects() {
        if (!reverseOrder) {
            return dNObjectsForward;
        }
        return getDnObjectsReverse();
    }
    /**
     * Returns the dnObjects (forward or reverse). 
     * ldaproder = true is the default order in EJBCA. 
     */
    public static String[]getDnObjects(boolean ldaporder) {
        if (ldaporder) {
            return dNObjectsForward;
        }
        return getDnObjectsReverse();
    }
    
    /**
     * Returns the reversed dnObjects.
     * Protected to allow testing
     */
    protected static String[] getDnObjectsReverse() {
        // Create and reverse the order if it has not been initialized already
        if (dNObjectsReverse == null) {
        	// this cast is not needed in java 5, but is needed for java 1.4
            dNObjectsReverse = (String[])dNObjectsForward.clone();
            ArrayUtils.reverse(dNObjectsReverse);
        }
        return dNObjectsReverse;
    }
    
    private static void load() {
    	loadOrdering();
    	loadMappings();
    }
    /**
     * Load DN ordering used in CertTools.stringToBCDNString etc.
     * Loads from file placed in src/dncomponents.properties
     * 
     * A line is:
     * DNName;DNid;ProfileName;ProfileId,ErrorString,LanguageConstant
     *
     */
    private static void loadMappings() {
        // Read the file to an array of lines 
        String line;
        
        BufferedReader in = null;
        InputStreamReader inf = null;
        try
        {    
            InputStream is = obj.getClass().getResourceAsStream("/profilemappings.properties");
            //log.info("is is: " + is);
            if (is != null) {
                inf = new InputStreamReader(is);
                //inf = new FileReader("c:\\foo.properties");
                in = new BufferedReader(inf);
                if (!in.ready())
                    throw new IOException();
                String[] splits = null;
                int lines = 0;
                ArrayList dnids = new ArrayList();
                ArrayList profileids = new ArrayList();
                while ((line = in.readLine()) != null) {
                	if (!line.startsWith("#")) { // # is a comment line
                        splits = StringUtils.split(line, ';');
                        if ( (splits != null) && (splits.length > 5) ) {
                        	String type = splits[0];
                            String dnname = splits[1]; 
                            Integer dnid = new Integer(splits[2]); 
                            String profilename = splits[3]; 
                            Integer profileid = new Integer(splits[4]); 
                            String errstr = splits[5]; 
                            String langstr = splits[6];
                            if (dnids.contains(dnid)) {
                            	log.error("Duplicated DN Id " + dnid + " detected in mapping file.");
                            } else {
                            	dnids.add(dnid);
                            }
                            if (profileids.contains(profileid)) {
                            	log.error("Duplicated Profile Id " + profileid + " detected in mapping file.");
                            } else {
                            	profileids.add(profileid);
                            }
                            // Fill maps
                            dnNameIdMap.put(dnname, dnid);
                            profileNameIdMap.put(profilename, profileid);
                            dnIdToProfileNameMap.put(dnid, profilename);
                            dnIdToProfileIdMap.put(dnid, profileid);
                            dnIdErrorMap.put(dnid, errstr);
                            profileIdToDnIdMap.put(profileid, dnid);
                            dnErrorTextMap.put(dnid, errstr);
                            profileNameLanguageMap.put(profilename, langstr);
                            profileIdLanguageMap.put(profileid, langstr);
                            if (type.equals("DN")) {
                            	dnProfileFields.add(profilename);
                            	dnLanguageTexts.add(langstr);
                            	dnDnIds.add(dnid);
                            	dnExtractorFields.add(dnname+"=");
                            	dnIdToExtractorFieldMap.put(dnid, dnname+"=");
                            }
                            if (type.equals("ALTNAME")) {
                            	altNameFields.add(dnname);
                            	altNameLanguageTexts.add(langstr);
                            	altNameDnIds.add(dnid);
                            	altNameExtractorFields.add(dnname+"=");
                            	altNameIdToExtractorFieldMap.put(dnid, dnname+"=");
                            }
                            if (type.equals("DIRATTR")) {
                            	dirAttrFields.add(dnname);
                            	dirAttrLanguageTexts.add(langstr);
                            	dirAttrDnIds.add(dnid);
                            	dirAttrExtractorFields.add(dnname+"=");
                            	dirAttrIdToExtractorFieldMap.put(dnid, dnname+"=");
                            }
                            lines++;
                        }                		
                	}
                }
                in.close();
                log.debug("Read profile maps with "+lines+" lines.");
            } else {
            	throw new IOException("Input stream for /profilemappings.properties is null");
            }
        }
        catch (IOException e) {
            log.error("Can not load profile mappings: ", e);
        } finally {
            try {
                if (inf != null) inf.close();
                if (in != null) in.close();                
            } catch (IOException e) {}
        }

    }
    /**
     * Load DN ordering used in CertTools.stringToBCDNString etc.
     * Loads from file placed in src/dncomponents.properties
     *
     */
    private static void loadOrdering() {
        // Read the file to an array of lines 
        String line;
        LinkedHashMap map = new LinkedHashMap();
        BufferedReader in = null;
        InputStreamReader inf = null;
        try
        {    
            InputStream is = obj.getClass().getResourceAsStream("/dncomponents.properties");
            //log.info("is is: " + is);
            if (is != null) {
                inf = new InputStreamReader(is);
                //inf = new FileReader("c:\\foo.properties");
                in = new BufferedReader(inf);
                if (!in.ready())
                    throw new IOException();
                String[] splits = null;
                while ((line = in.readLine()) != null) {
                	if (!line.startsWith("#")) { // # is a comment line
                		splits = StringUtils.split(line, '=');
                		if ( (splits != null) && (splits.length > 1) ) {
                			String name = splits[0]; 
                			DERObjectIdentifier oid = new DERObjectIdentifier(splits[1]);
                			map.put(name, oid);
                		}
                	}
                }
                in.close();
                // Now we have read it in, transfer it to the main oid map
                log.info("Using DN components from properties file");
                oids.clear();
                oids.putAll(map);
                Set keys = map.keySet();
                // Set the maps to the desired ordering
                dNObjectsForward = (String[])keys.toArray(new String[0]);                
            } else {
                log.debug("Using default values for DN components");                
            }
        }
        catch (IOException e) {
            log.debug("Using default values for DN components");
        } finally {
            try {
                if (inf != null) inf.close();
                if (in != null) in.close();                
            } catch (IOException e) {}
        }

    }

}
