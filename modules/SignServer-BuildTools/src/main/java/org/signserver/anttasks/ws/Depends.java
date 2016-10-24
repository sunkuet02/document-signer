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
package org.signserver.anttasks.ws;

import org.apache.tools.ant.types.DataType;

/**
 * Element of the WsImport task.
 * 
 * @author Markus Kilås
 * @version $Id: Depends.java 2048 2011-12-21 10:12:55Z netmackan $
 */
public class Depends extends DataType {
    
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
}
