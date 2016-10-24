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
package org.signserver.client.cli.defaultimpl;

import org.ejbca.ui.cli.util.ConsolePasswordReader;

/**
 * Interface for providers of a ConsolePasswordReader.
 *
 * @author Markus Kilås
 * @version $Id: ConsolePasswordProvider.java 6019 2015-05-11 08:31:54Z netmackan $
 */
public interface ConsolePasswordProvider {
    ConsolePasswordReader createConsolePasswordReader();
}
