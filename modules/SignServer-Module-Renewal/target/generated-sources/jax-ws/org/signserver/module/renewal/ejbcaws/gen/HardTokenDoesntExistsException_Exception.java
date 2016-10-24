
package org.signserver.module.renewal.ejbcaws.gen;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "HardTokenDoesntExistsException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class HardTokenDoesntExistsException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private HardTokenDoesntExistsException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public HardTokenDoesntExistsException_Exception(String message, HardTokenDoesntExistsException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public HardTokenDoesntExistsException_Exception(String message, HardTokenDoesntExistsException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.signserver.module.renewal.ejbcaws.gen.HardTokenDoesntExistsException
     */
    public HardTokenDoesntExistsException getFaultInfo() {
        return faultInfo;
    }

}
