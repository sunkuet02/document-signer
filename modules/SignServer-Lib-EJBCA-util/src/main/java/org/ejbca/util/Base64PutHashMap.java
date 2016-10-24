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

import java.util.HashMap;
import java.util.Map;


/** An implementation of HashMap that base64 encodes all String's that you 'put', 
 * it encodes them to form "B64:<base64 encoded string>". It only encodes objects of type String.
 * 
 * @author tomasg
 * @version $Id: Base64PutHashMap.java 5585 2008-05-01 20:55:00Z anatom $
 */
public class Base64PutHashMap extends HashMap {
    public Base64PutHashMap() {
        super();
    }
    public Base64PutHashMap(Map m) {
        super(m);
    }
    public Object put(Object key, Object value) {
        if (value == null) {
            return super.put(key, value);
        }
        if (value instanceof String) {
            String s = StringTools.putBase64String((String)value);
            return super.put(key,s);
        }
        return super.put(key, value);
    }
    
}
