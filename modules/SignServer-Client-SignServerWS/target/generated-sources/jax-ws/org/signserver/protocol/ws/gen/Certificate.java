
package org.signserver.protocol.ws.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for certificate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="certificate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="certType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="certificateBase64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provider" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "certificate", propOrder = {
    "certType",
    "certificateBase64",
    "provider"
})
public class Certificate {

    protected String certType;
    protected String certificateBase64;
    protected String provider;

    /**
     * Gets the value of the certType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertType() {
        return certType;
    }

    /**
     * Sets the value of the certType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertType(String value) {
        this.certType = value;
    }

    /**
     * Gets the value of the certificateBase64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateBase64() {
        return certificateBase64;
    }

    /**
     * Sets the value of the certificateBase64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateBase64(String value) {
        this.certificateBase64 = value;
    }

    /**
     * Gets the value of the provider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvider() {
        return provider;
    }

    /**
     * Sets the value of the provider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvider(String value) {
        this.provider = value;
    }

}
