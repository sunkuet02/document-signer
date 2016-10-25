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
package org.signserver.clientws;

import org.signserver.admin.cli.spi.AdminCommandFactory;
import org.signserver.cli.CommandLineInterface;
import org.signserver.cli.spi.UnexpectedCommandFailureException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Implements the SignServer command line interface.
 *
 * @version $Id: Worker.java 2102 2012-02-07 19:25:44Z netmackan $
 */
public class Worker extends CommandLineInterface {

    public Worker() {
        super(AdminCommandFactory.class, null);
    }

    public int createWorker(Properties properties) throws UnexpectedCommandFailureException {
        Worker adminCommandLine = new Worker();
        ArrayList<String> arguments = createStringArgumentsFromProperties(properties);
        String [] argumentsToSend = new String[arguments.size()];
        return adminCommandLine.createWorker(arguments.toArray(argumentsToSend));
    }

    private ArrayList<String> createStringArgumentsFromProperties(Properties properties) {
        ArrayList <String> arrayList = new ArrayList<String>();

        arrayList.add("setproperties");
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            arrayList.add(key);
            arrayList.add(value);
        }
        return arrayList;
    }

}
