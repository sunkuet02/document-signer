
package org.signserver.client.clientws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="archiveId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="metadata" type="{http://clientws.signserver.org/}metadata" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="requestId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="signerCertificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataResponse", propOrder = {
    "archiveId",
    "data",
    "metadata",
    "requestId",
    "signerCertificate"
})
@XmlSeeAlso({
    SodResponse.class
})
public class DataResponse {

    protected String archiveId;
    protected byte[] data;
    @XmlElement(nillable = true)
    protected List<Metadata> metadata;
    protected int requestId;
    protected byte[] signerCertificate;

    /**
     * Gets the value of the archiveId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArchiveId() {
        return archiveId;
    }

    /**
     * Sets the value of the archiveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchiveId(String value) {
        this.archiveId = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setData(byte[] value) {
        this.data = value;
    }

    /**
     * Gets the value of the metadata property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metadata property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetadata().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Metadata }
     * 
     * 
     */
    public List<Metadata> getMetadata() {
        if (metadata == null) {
            metadata = new ArrayList<Metadata>();
        }
        return this.metadata;
    }

    /**
     * Gets the value of the requestId property.
     * 
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     * 
     */
    public void setRequestId(int value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the signerCertificate property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getSignerCertificate() {
        return signerCertificate;
    }

    /**
     * Sets the value of the signerCertificate property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setSignerCertificate(byte[] value) {
        this.signerCertificate = value;
    }

}
