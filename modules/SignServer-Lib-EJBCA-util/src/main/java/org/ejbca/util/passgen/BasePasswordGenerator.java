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

package org.ejbca.util.passgen;
import java.util.Random;

/**
 * BasePasswordGenerator is a baseclass for generating random passwords.
 * Inheriting classes should overload the constants USEDCHARS, MIN_CHARS
 * and MAX_CHARS.
 *
 * @version $Id: BasePasswordGenerator.java 6998 2009-02-20 15:45:19Z anatom $
 */
public abstract class BasePasswordGenerator implements IPasswordGenerator{

    protected BasePasswordGenerator(char[] usedchars){

       this.usedchars = usedchars;
    }

	/**
	 * @see org.ejbca.util.passgen.IPasswordGenerator
	 */

	public String getNewPassword(int minlength, int maxlength){
		int difference = maxlength - minlength;
		char[] password = null;

		Random ran = new Random();

		// Calculate the length of password
		int passlen = maxlength;
		if(minlength != maxlength) {
		  passlen = minlength + ran.nextInt(difference);
		}
		password = new char[passlen];
		for(int i=0; i < passlen; i++){
		  password[i] = usedchars[ran.nextInt(usedchars.length)];
		}

		return new String(password);
	}


    private final char[] usedchars;
}
