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

package org.ejbca.util;

/**
 * 
 * @version $Id: IPatternLogger.java 8356 2009-11-27 15:23:31Z anatom $
 *
 */
public interface IPatternLogger {

	public static final String LOG_TIME = "LOG_TIME";// The Date and time the request.
	public static final String LOG_ID = "LOG_ID"; //An integer identifying a log entry for a request
	public static final String SESSION_ID = "SESSION_ID"; //A random 32 bit number identifying a log entry for a request
	/** REPLY_TIME is a marker that is used to record the total time a request takes to process. It is replaced with 
	 * the correct value when the log entry is written.
	 * @see org.ejbca.util.PatternLogger.flush()
	 */
    public static final String REPLY_TIME = "REPLY_TIME";
	

	/**
	 * Hex-encodes the bytes.
	 * method that makes sure that a "" is inserted instead of null
	 * @param key
	 * @param value
	 */
	public void paramPut(String key, byte[] value);

	/**
	 * method that makes sure that a "" is inserted instead of null
	 * @param key
	 * @param value
	 */
	public void paramPut(String key, String value);

	/**
	 * method that makes sure that a "" is inserted instead of null
	 * @param key
	 * @param value
	 */
	public void paramPut(String key, Integer value);
	
	/**
	 * Method used for creating a log row of all added values
	 */
	public void writeln();
	
    /**
     * Writes all the rows created by writeln() to the Logger
     */
    public void flush();
}
