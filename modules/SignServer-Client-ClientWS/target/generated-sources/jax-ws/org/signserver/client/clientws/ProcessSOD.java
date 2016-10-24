
package org.signserver.client.clientws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for processSOD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="processSOD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="worker" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadata" type="{http://clientws.signserver.org/}metadata" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sodData" type="{http://clientws.signserver.org/}sodRequest" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processSOD", propOrder = {
    "worker",
    "metadata",
    "sodData"
})
public class ProcessSOD {

    protected String worker;
    protected List<Metadata> metadata;
    protected SodRequest sodData;

    /**
     * Gets the value of the worker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorker() {
        return worker;
    }

    /**
     * Sets the value of the worker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorker(String value) {
        this.worker = value;
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
     * Gets the value of the sodData property.
     * 
     * @return
     *     possible object is
     *     {@link SodRequest }
     *     
     */
    public SodRequest getSodData() {
        return sodData;
    }

    /**
     * Sets the value of the sodData property.
     * 
     * @param value
     *     allowed object is
     *     {@link SodRequest }
     *     
     */
    public void setSodData(SodRequest value) {
        this.sodData = value;
    }

}
