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
 
package org.ejbca.core.model;

/**
 * Constants for users and certificates. Constants for Type of user: Type is constructed as a mask
 * since one user can be of several types. To test a user type:
 * <pre>
 * if (((type & USER_ENDUSER) == USER_ENDUSER) && ((type & USER_CAADMIN) == USER_ADMINISTOR) || ...
 *    ...
 * </pre>
 * Bit usage: bits 0-7   (1:st byte):  user types bits 8-15  (2:nd byte):  unused bits 16-23 (3:rd
 * byte):  unused bits 24-30 (4:th byte):  unused Constants for certificates are simple integer
 * types. Constants for Token Types Token type is constructed of integer constants since only one
 * token type can be generated.
 *
 * @version $Id: SecConst.java 8380 2009-11-30 15:28:15Z anatom $
 */
public class SecConst extends Object {
    // User types

    /** Dummy type. */
    public static final int USER_INVALID = 0x0;

    /** This is an end user certificate (default). */
    public static final int USER_ENDUSER = 0x1;

    /** This user is an administrator. */
    public static final int USER_ADMINISTRATOR = 0x40;

    /** This users keystores are key recoverable. */
    public static final int USER_KEYRECOVERABLE = 0x80;

    
    /** Notification will be sent to this users emailaddress */
    public static final int USER_SENDNOTIFICATION = 0x100;
    
    /** Registration data will be printed for this user */
    public static final int USER_PRINT = 0x200;


    // Token types.

    /** Indicates that a browser generated token should be used. */
    public static final int TOKEN_SOFT_BROWSERGEN = 1;

    /** Indicates that a p12 token should be generated. */
    public static final int TOKEN_SOFT_P12 = 2;

    /** Indicates that a jks token should be generated. */
    public static final int TOKEN_SOFT_JKS = 3;

    /** Indicates that a pem token should be generated. */
    public static final int TOKEN_SOFT_PEM = 4;

    /** All values equal or below this constant should be treated as a soft token. */
    public static final int TOKEN_SOFT = 100;
    
    public static final String[] TOKENTEXTS = {"TOKENSOFTBROWSERGEN","TOKENSOFTP12","TOKENSOFTJKS","TOKENSOFTPEM"};
    
    public static final int[]    TOKENIDS   = {SecConst.TOKEN_SOFT_BROWSERGEN,SecConst.TOKEN_SOFT_P12,SecConst.TOKEN_SOFT_JKS,SecConst.TOKEN_SOFT_PEM};

    /** Constant indicating a standard hard token, defined in scaper. */
    public static final int TOKEN_HARD_DEFAULT = 101;

    /** Constant indicating a eid hard token.  
     *   OBSERVE This class should only be used for backward compability with EJBCA 2.0
     */
    public static final int TOKEN_EID = 102;
    
    /**Constant indicating a swedish eid hard token.  */
    public static final int TOKEN_SWEDISHEID = 103;

    /**Constant indicating a enhanced eid hard token.  */
    public static final int TOKEN_ENHANCEDEID = 104;
    
    /**Constant indicating a enhanced eid hard token.  */
    public static final int TOKEN_TURKISHEID = 105;
    
    // Certificate profiles.

    public static final int NO_HARDTOKENISSUER            = 0;

    /** Used when no certificate profile id value is available */
    public static final int CERTPROFILE_NO_PROFILE            = 0; 
    public static final int CERTPROFILE_FIXED_ENDUSER         = 1;
    public static final int CERTPROFILE_FIXED_SUBCA           = 2;
    public static final int CERTPROFILE_FIXED_ROOTCA          = 3;
	public static final int CERTPROFILE_FIXED_OCSPSIGNER      = 4;
	public static final int CERTPROFILE_FIXED_HARDTOKENAUTH   = 5;
	public static final int CERTPROFILE_FIXED_HARDTOKENAUTHENC= 6;
	public static final int CERTPROFILE_FIXED_HARDTOKENENC    = 7;
	public static final int CERTPROFILE_FIXED_HARDTOKENSIGN   = 8;
    public static final int CERTPROFILE_FIXED_SERVER          = 9;

    public static final int EMPTY_ENDENTITYPROFILE = 1;

    /** Used in end entity profiles and service workers */
    public static final int ALLCAS = 1;
        
    /**
     * Constants defining range of id's reserved for fixed certificate types. Observe fixed
     * certificates cannot have value 0.
     */
    public static final int FIXED_CERTIFICATEPROFILE_BOUNDRY = 1000;
    public static final int PROFILE_NO_PROFILE = 0;

    
    /**
     * Constants used in the RSASignSessionBean indicating the userdata defined CA should be used.
     */
    public static final int CAID_USEUSERDEFINED = 0;

    /** Constant used to determine the size of the result from SQL select queries */
    public static final int MAXIMUM_QUERY_ROWCOUNT = 500; 
    
    
    /** Constants used to indicate status of a CA. */
    public static final int CA_ACTIVE = 1;
    public static final int CA_WAITING_CERTIFICATE_RESPONSE = 2;
    public static final int CA_EXPIRED = 3;
    public static final int CA_REVOKED = 4;
    public static final int CA_OFFLINE = 5;
    public static final int CA_EXTERNAL = 6;
    
    /** signs certificates issued by the CA */
    public static final int CAKEYPURPOSE_CERTSIGN            = 1;
    /** igns CRLs issues by the CA */
    public static final int CAKEYPURPOSE_CRLSIGN             = 2;
    /** encrypts entity keys stored in the database for key recovery */
    public static final int CAKEYPURPOSE_KEYENCRYPT          = 3;
    /** used for testing if the CA token is functioning and on-line */
    public static final int CAKEYPURPOSE_KEYTEST             = 4;
    /** encrypts hard token PIN/PUK codes etc */
    public static final int CAKEYPURPOSE_HARDTOKENENCRYPT    = 5;
    /** The CAs previous signing key, if any exists */
    public static final int CAKEYPURPOSE_CERTSIGN_PREVIOUS   = 6;

    /**
     * Default key lengths. Users are allowed to choose from these key lengths when
     * lookup of available bit lengths fails. 
     * @see org.ejbca.ui.web.pub.ApplyBean
     */
    public static final int[] DEFAULT_KEY_LENGTHS = new int[] {512, 1024, 2048};

    /**
     * Prevents creation of new SecConst
     */
    private SecConst() {
    }
    
    /**
     * @return true is certificate profile identified by profileId is fixed
     */
    public static boolean isFixedCertificateProfile(int profileId) {
    	return (
    			profileId == SecConst.CERTPROFILE_FIXED_ENDUSER ||
    			profileId == SecConst.CERTPROFILE_FIXED_SUBCA ||
    			profileId == SecConst.CERTPROFILE_FIXED_ROOTCA ||
    			profileId == SecConst.CERTPROFILE_FIXED_HARDTOKENAUTH ||
    			profileId == SecConst.CERTPROFILE_FIXED_HARDTOKENAUTHENC ||
    			profileId == SecConst.CERTPROFILE_FIXED_HARDTOKENENC ||
    			profileId == SecConst.CERTPROFILE_FIXED_HARDTOKENSIGN ||
    			profileId == SecConst.CERTPROFILE_FIXED_OCSPSIGNER ||
    			profileId == SecConst.CERTPROFILE_FIXED_SERVER );
    }
    

}