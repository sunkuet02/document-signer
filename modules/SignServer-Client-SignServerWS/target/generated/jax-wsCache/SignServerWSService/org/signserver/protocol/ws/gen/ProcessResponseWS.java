
package org.signserver.protocol.ws.gen;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for processResponseWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="processResponseWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="requestID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="responseDataBase64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workerCertificate" type="{gen.ws.protocol.signserver.org}certificate" minOccurs="0"/>
 *         &lt;element name="workerCertificateChain" type="{gen.ws.protocol.signserver.org}certificate" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processResponseWS", propOrder = {
    "requestID",
    "responseDataBase64",
    "workerCertificate",
    "workerCertificateChain"
})
public class ProcessResponseWS {

    protected int requestID;
    protected String responseDataBase64;
    protected Certificate workerCertificate;
    @XmlElement(nillable = true)
    protected List<Certificate> workerCertificateChain;

    /**
     * Gets the value of the requestID property.
     * 
     */
    public int getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     */
    public void setRequestID(int value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the responseDataBase64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseDataBase64() {
        return responseDataBase64;
    }

    /**
     * Sets the value of the responseDataBase64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseDataBase64(String value) {
        this.responseDataBase64 = value;
    }

    /**
     * Gets the value of the workerCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link Certificate }
     *     
     */
    public Certificate getWorkerCertificate() {
        return workerCertificate;
    }

    /**
     * Sets the value of the workerCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Certificate }
     *     
     */
    public void setWorkerCertificate(Certificate value) {
        this.workerCertificate = value;
    }

    /**
     * Gets the value of the workerCertificateChain property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the workerCertificateChain property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorkerCertificateChain().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Certificate }
     * 
     * 
     */
    public List<Certificate> getWorkerCertificateChain() {
        if (workerCertificateChain == null) {
            workerCertificateChain = new ArrayList<Certificate>();
        }
        return this.workerCertificateChain;
    }

}
