
package org.signserver.protocol.ws.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.signserver.protocol.ws.gen package. 
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

    private final static QName _GetStatus_QNAME = new QName("gen.ws.protocol.signserver.org", "getStatus");
    private final static QName _Process_QNAME = new QName("gen.ws.protocol.signserver.org", "process");
    private final static QName _IllegalRequestException_QNAME = new QName("gen.ws.protocol.signserver.org", "IllegalRequestException");
    private final static QName _GetStatusResponse_QNAME = new QName("gen.ws.protocol.signserver.org", "getStatusResponse");
    private final static QName _ProcessResponse_QNAME = new QName("gen.ws.protocol.signserver.org", "processResponse");
    private final static QName _CryptoTokenOfflineException_QNAME = new QName("gen.ws.protocol.signserver.org", "CryptoTokenOfflineException");
    private final static QName _SignServerException_QNAME = new QName("gen.ws.protocol.signserver.org", "SignServerException");
    private final static QName _InvalidWorkerIdException_QNAME = new QName("gen.ws.protocol.signserver.org", "InvalidWorkerIdException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.signserver.protocol.ws.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessRequestWS }
     * 
     */
    public ProcessRequestWS createProcessRequestWS() {
        return new ProcessRequestWS();
    }

    /**
     * Create an instance of {@link ProcessRequestWS.RequestMetadata }
     * 
     */
    public ProcessRequestWS.RequestMetadata createProcessRequestWSRequestMetadata() {
        return new ProcessRequestWS.RequestMetadata();
    }

    /**
     * Create an instance of {@link Process }
     * 
     */
    public Process createProcess() {
        return new Process();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link IllegalRequestException }
     * 
     */
    public IllegalRequestException createIllegalRequestException() {
        return new IllegalRequestException();
    }

    /**
     * Create an instance of {@link ProcessResponse }
     * 
     */
    public ProcessResponse createProcessResponse() {
        return new ProcessResponse();
    }

    /**
     * Create an instance of {@link CryptoTokenOfflineException }
     * 
     */
    public CryptoTokenOfflineException createCryptoTokenOfflineException() {
        return new CryptoTokenOfflineException();
    }

    /**
     * Create an instance of {@link SignServerException }
     * 
     */
    public SignServerException createSignServerException() {
        return new SignServerException();
    }

    /**
     * Create an instance of {@link InvalidWorkerIdException }
     * 
     */
    public InvalidWorkerIdException createInvalidWorkerIdException() {
        return new InvalidWorkerIdException();
    }

    /**
     * Create an instance of {@link Certificate }
     * 
     */
    public Certificate createCertificate() {
        return new Certificate();
    }

    /**
     * Create an instance of {@link WorkerStatusWS }
     * 
     */
    public WorkerStatusWS createWorkerStatusWS() {
        return new WorkerStatusWS();
    }

    /**
     * Create an instance of {@link ProcessResponseWS }
     * 
     */
    public ProcessResponseWS createProcessResponseWS() {
        return new ProcessResponseWS();
    }

    /**
     * Create an instance of {@link ProcessRequestWS.RequestMetadata.Entry }
     * 
     */
    public ProcessRequestWS.RequestMetadata.Entry createProcessRequestWSRequestMetadataEntry() {
        return new ProcessRequestWS.RequestMetadata.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "getStatus")
    public JAXBElement<GetStatus> createGetStatus(GetStatus value) {
        return new JAXBElement<GetStatus>(_GetStatus_QNAME, GetStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Process }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "process")
    public JAXBElement<Process> createProcess(Process value) {
        return new JAXBElement<Process>(_Process_QNAME, Process.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalRequestException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "IllegalRequestException")
    public JAXBElement<IllegalRequestException> createIllegalRequestException(IllegalRequestException value) {
        return new JAXBElement<IllegalRequestException>(_IllegalRequestException_QNAME, IllegalRequestException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "getStatusResponse")
    public JAXBElement<GetStatusResponse> createGetStatusResponse(GetStatusResponse value) {
        return new JAXBElement<GetStatusResponse>(_GetStatusResponse_QNAME, GetStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "processResponse")
    public JAXBElement<ProcessResponse> createProcessResponse(ProcessResponse value) {
        return new JAXBElement<ProcessResponse>(_ProcessResponse_QNAME, ProcessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CryptoTokenOfflineException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "CryptoTokenOfflineException")
    public JAXBElement<CryptoTokenOfflineException> createCryptoTokenOfflineException(CryptoTokenOfflineException value) {
        return new JAXBElement<CryptoTokenOfflineException>(_CryptoTokenOfflineException_QNAME, CryptoTokenOfflineException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SignServerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "SignServerException")
    public JAXBElement<SignServerException> createSignServerException(SignServerException value) {
        return new JAXBElement<SignServerException>(_SignServerException_QNAME, SignServerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidWorkerIdException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "gen.ws.protocol.signserver.org", name = "InvalidWorkerIdException")
    public JAXBElement<InvalidWorkerIdException> createInvalidWorkerIdException(InvalidWorkerIdException value) {
        return new JAXBElement<InvalidWorkerIdException>(_InvalidWorkerIdException_QNAME, InvalidWorkerIdException.class, null, value);
    }

}
