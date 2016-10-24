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
package org.signserver.admin.cli.defaultimpl;

import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import org.signserver.cli.spi.CommandFailureException;
import org.signserver.cli.spi.IllegalCommandArgumentsException;
import org.signserver.cli.spi.UnexpectedCommandFailureException;

/**
 * Sets properties from a given property file.
 * <p>
 * See the manual for the syntax of the property file
 *
 * @version $Id: SetPropertiesCommand.java 2095 2012-02-06 16:06:07Z netmackan $
 */
public class SetPropertiesCommand extends AbstractAdminCommand {

    @Override
    public String getDescription() {
        return "Sets properties from a given property file";
    }

    @Override
    public String getUsages() {
        return "Usage: signserver setproperties <propertyfile>\n"
                + "Example 1: signserver setproperties mysettings.properties\n"
                + "Example 2: signserver setproperties -host node3.someorg.com mysettings.properties\n\n";
    }

    public int execute(String... args) throws IllegalCommandArgumentsException, CommandFailureException, UnexpectedCommandFailureException {
        if (args.length == 1) {
            try {
                SetPropertiesHelper helper = new SetPropertiesHelper(getOutputStream());
                Properties properties = loadProperties(args[0]);

                getOutputStream().println("Configuring properties as defined in the file : " + args[0]);
                helper.process(properties);

                this.getOutputStream().println("\n\n");
                return 0;
            } catch (Exception e) {
                throw new UnexpectedCommandFailureException(e);
            }
        } else {
            try {
                Properties properties = new Properties();

                String key = null, value = null;
                int argumentCounter = 0;
                for (String str : args) {
                    if (argumentCounter % 2 == 0) {
                        key = str;
                    } else {
                        value = str;
                        properties.setProperty(key, value);
                    }
                    argumentCounter++;
                }

                SetPropertiesHelper helper = new SetPropertiesHelper(getOutputStream());
                helper.process(properties);

                int workerID =  helper.getWorkerID();
                getWorkerSession().reloadConfiguration(workerID);
                return workerID;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  0;
    }

    private Properties loadProperties(String path) {
        Properties retval = new Properties();
        try {
            retval.load(new FileInputStream(path));
        } catch (Exception e) {
            getOutputStream().println("Error reading property file : " + path);
            System.exit(-1);
        }

        return retval;
    }
}
