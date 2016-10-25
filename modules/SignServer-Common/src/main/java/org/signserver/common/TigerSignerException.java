package org.signserver.common;

import javax.xml.ws.WebFault;

/**
 * Created by sun on 10/17/16.
 */
@WebFault
public class TigerSignerException extends Exception{
    private static final long serialVersionUID = 1L;

    public TigerSignerException(String message) {
        super(message);
    }

    public TigerSignerException(String message, Throwable e) {
        super(message, e);
    }
}
