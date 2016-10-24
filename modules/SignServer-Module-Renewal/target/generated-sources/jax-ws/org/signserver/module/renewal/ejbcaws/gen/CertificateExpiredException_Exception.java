
package org.signserver.module.renewal.ejbcaws.gen;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "CertificateExpiredException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class CertificateExpiredException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private CertificateExpiredException faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public CertificateExpiredException_Exception(String message, CertificateExpiredException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public CertificateExpiredException_Exception(String message, CertificateExpiredException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.signserver.module.renewal.ejbcaws.gen.CertificateExpiredException
     */
    public CertificateExpiredException getFaultInfo() {
        return faultInfo;
    }

}
