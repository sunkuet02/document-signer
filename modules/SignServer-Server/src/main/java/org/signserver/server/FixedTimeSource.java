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
package org.signserver.server;

import java.util.Date;
import java.util.Properties;

/**
 * Simple class implementing the ITimeSource interface always returns the
 * configured fixed time. This is mainly intended to use for testing to get a
 * predictable non-null time value.
 *
 * @author Markus Kilås
 * @version $Id: FixedTimeSource.java 5796 2015-03-05 12:56:29Z netmackan $
 */
public class FixedTimeSource implements ITimeSource {

    public static final String FIXEDTIME = "FIXEDTIME";
    
    private Date time;
    
    @Override
    public void init(Properties props) {
        time = new Date(Long.parseLong(props.getProperty(FIXEDTIME, "0")));
    }

    @Override
    public Date getGenTime() {
        return time;
    }

}
