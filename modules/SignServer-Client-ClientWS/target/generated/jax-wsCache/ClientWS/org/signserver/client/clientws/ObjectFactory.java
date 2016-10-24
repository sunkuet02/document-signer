
package org.signserver.client.clientws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.signserver.client.clientws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _InternalServerException_QNAME = new QName("http://clientws.signserver.org/", "InternalServerException");
    private final static QName _ProcessDataResponse_QNAME = new QName("http://clientws.signserver.org/", "processDataResponse");
    private final static QName _ProcessSOD_QNAME = new QName("http://clientws.signserver.org/", "processSOD");
    private final static QName _ProcessData_QNAME = new QName("http://clientws.signserver.org/", "processData");
    private final static QName _RequestFailedException_QNAME = new QName("http://clientws.signserver.org/", "RequestFailedException");
    private final static QName _ProcessSODResponse_QNAME = new QName("http://clientws.signserver.org/", "processSODResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.signserver.client.clientws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InternalServerException }
     * 
     */
    public InternalServerException createInternalServerException() {
        return new InternalServerException();
    }

    /**
     * Create an instance of {@link ProcessSOD }
     * 
     */
    public ProcessSOD createProcessSOD() {
        return new ProcessSOD();
    }

    /**
     * Create an instance of {@link ProcessData }
     * 
     */
    public ProcessData createProcessData() {
        return new ProcessData();
    }

    /**
     * Create an instance of {@link ProcessSODResponse }
     * 
     */
    public ProcessSODResponse createProcessSODResponse() {
        return new ProcessSODResponse();
    }

    /**
     * Create an instance of {@link RequestFailedException }
     * 
     */
    public RequestFailedException createRequestFailedException() {
        return new RequestFailedException();
    }

    /**
     * Create an instance of {@link ProcessDataResponse }
     * 
     */
    public ProcessDataResponse createProcessDataResponse() {
        return new ProcessDataResponse();
    }

    /**
     * Create an instance of {@link SodResponse }
     * 
     */
    public SodResponse createSodResponse() {
        return new SodResponse();
    }

    /**
     * Create an instance of {@link SodRequest }
     * 
     */
    public SodRequest createSodRequest() {
        return new SodRequest();
    }

    /**
     * Create an instance of {@link DataGroup }
     * 
     */
    public DataGroup createDataGroup() {
        return new DataGroup();
    }

    /**
     * Create an instance of {@link DataResponse }
     * 
     */
    public DataResponse createDataResponse() {
        return new DataResponse();
    }

    /**
     * Create an instance of {@link Metadata }
     * 
     */
    public Metadata createMetadata() {
        return new Metadata();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InternalServerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://clientws.signserver.org/", name = "InternalServerException")
    public JAXBElement<InternalServerException> createInternalServerException(InternalServerException value) {
        return new JAXBElement<InternalServerException>(_InternalServerException_QNAME, InternalServerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://clientws.signserver.org/", name = "processDataResponse")
    public JAXBElement<ProcessDataResponse> createProcessDataResponse(ProcessDataResponse value) {
        return new JAXBElement<ProcessDataResponse>(_ProcessDataResponse_QNAME, ProcessDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessSOD }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://clientws.signserver.org/", name = "processSOD")
    public JAXBElement<ProcessSOD> createProcessSOD(ProcessSOD value) {
        return new JAXBElement<ProcessSOD>(_ProcessSOD_QNAME, ProcessSOD.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://clientws.signserver.org/", name = "processData")
    public JAXBElement<ProcessData> createProcessData(ProcessData value) {
        return new JAXBElement<ProcessData>(_ProcessData_QNAME, ProcessData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestFailedException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://clientws.signserver.org/", name = "RequestFailedException")
    public JAXBElement<RequestFailedException> createRequestFailedException(RequestFailedException value) {
        return new JAXBElement<RequestFailedException>(_RequestFailedException_QNAME, RequestFailedException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessSODResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://clientws.signserver.org/", name = "processSODResponse")
    public JAXBElement<ProcessSODResponse> createProcessSODResponse(ProcessSODResponse value) {
        return new JAXBElement<ProcessSODResponse>(_ProcessSODResponse_QNAME, ProcessSODResponse.class, null, value);
    }

}
