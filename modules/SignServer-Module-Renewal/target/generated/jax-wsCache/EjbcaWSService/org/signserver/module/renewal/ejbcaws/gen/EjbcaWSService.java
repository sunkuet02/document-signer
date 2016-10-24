
package org.signserver.module.renewal.ejbcaws.gen;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "EjbcaWSService", targetNamespace = "http://ws.protocol.core.ejbca.org/", wsdlLocation = "null")
public class EjbcaWSService
    extends Service
{

    private final static URL EJBCAWSSERVICE_WSDL_LOCATION;
    private final static WebServiceException EJBCAWSSERVICE_EXCEPTION;
    private final static QName EJBCAWSSERVICE_QNAME = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService");

    static {
        EJBCAWSSERVICE_WSDL_LOCATION = org.signserver.module.renewal.ejbcaws.gen.EjbcaWSService.class.getResource("null");
        WebServiceException e = null;
        if (EJBCAWSSERVICE_WSDL_LOCATION == null) {
            e = new WebServiceException("Cannot find 'null' wsdl. Place the resource correctly in the classpath.");
        }
        EJBCAWSSERVICE_EXCEPTION = e;
    }

    public EjbcaWSService() {
        super(__getWsdlLocation(), EJBCAWSSERVICE_QNAME);
    }

    public EjbcaWSService(WebServiceFeature... features) {
        super(__getWsdlLocation(), EJBCAWSSERVICE_QNAME, features);
    }

    public EjbcaWSService(URL wsdlLocation) {
        super(wsdlLocation, EJBCAWSSERVICE_QNAME);
    }

    public EjbcaWSService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, EJBCAWSSERVICE_QNAME, features);
    }

    public EjbcaWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EjbcaWSService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns EjbcaWS
     */
    @WebEndpoint(name = "EjbcaWSPort")
    public EjbcaWS getEjbcaWSPort() {
        return super.getPort(new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSPort"), EjbcaWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns EjbcaWS
     */
    @WebEndpoint(name = "EjbcaWSPort")
    public EjbcaWS getEjbcaWSPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSPort"), EjbcaWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (EJBCAWSSERVICE_EXCEPTION!= null) {
            throw EJBCAWSSERVICE_EXCEPTION;
        }
        return EJBCAWSSERVICE_WSDL_LOCATION;
    }

}