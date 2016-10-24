
package org.signserver.module.renewal.ejbcaws.gen;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "AlreadyRevokedException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class AlreadyRevokedException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private AlreadyRevokedException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public AlreadyRevokedException_Exception(String message, AlreadyRevokedException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public AlreadyRevokedException_Exception(String message, AlreadyRevokedException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.signserver.module.renewal.ejbcaws.gen.AlreadyRevokedException
     */
    public AlreadyRevokedException getFaultInfo() {
        return faultInfo;
    }

}