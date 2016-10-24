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
package org.signserver.test.random.impl;

import org.signserver.common.RequestContext;

/**
 * Responsible for processing a RequestContext before it is sent out as a
 * request.
 * @author Markus Kilås
 * @version $Id: RequestContextPreProcessor.java 5550 2014-12-05 13:18:27Z netmackan $
 */
public interface RequestContextPreProcessor {

    /**
     * Called before the RequestContext is sent out in a request.
     * @param context to modify
     */
    void preProcess(RequestContext context);
}
