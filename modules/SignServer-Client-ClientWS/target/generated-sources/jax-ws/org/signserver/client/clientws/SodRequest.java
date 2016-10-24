
package org.signserver.client.clientws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sodRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sodRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataGroup" type="{http://clientws.signserver.org/}dataGroup" maxOccurs="unbounded"/>
 *         &lt;element name="ldsVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unicodeVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sodRequest", propOrder = {
    "dataGroup",
    "ldsVersion",
    "unicodeVersion"
})
public class SodRequest {

    @XmlElement(required = true)
    protected List<DataGroup> dataGroup;
    protected String ldsVersion;
    protected String unicodeVersion;

    /**
     * Gets the value of the dataGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataGroup }
     * 
     * 
     */
    public List<DataGroup> getDataGroup() {
        if (dataGroup == null) {
            dataGroup = new ArrayList<DataGroup>();
        }
        return this.dataGroup;
    }

    /**
     * Gets the value of the ldsVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLdsVersion() {
        return ldsVersion;
    }

    /**
     * Sets the value of the ldsVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLdsVersion(String value) {
        this.ldsVersion = value;
    }

    /**
     * Gets the value of the unicodeVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnicodeVersion() {
        return unicodeVersion;
    }

    /**
     * Sets the value of the unicodeVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnicodeVersion(String value) {
        this.unicodeVersion = value;
    }

}
