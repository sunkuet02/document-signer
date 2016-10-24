
package org.signserver.protocol.ws.gen;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for workerStatusWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="workerStatusWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errormessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overallStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "workerStatusWS", propOrder = {
    "errormessage",
    "overallStatus",
    "workerName"
})
public class WorkerStatusWS {

    protected String errormessage;
    protected String overallStatus;
    protected String workerName;

    /**
     * Gets the value of the errormessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrormessage() {
        return errormessage;
    }

    /**
     * Sets the value of the errormessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrormessage(String value) {
        this.errormessage = value;
    }

    /**
     * Gets the value of the overallStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverallStatus() {
        return overallStatus;
    }

    /**
     * Sets the value of the overallStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverallStatus(String value) {
        this.overallStatus = value;
    }

    /**
     * Gets the value of the workerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * Sets the value of the workerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkerName(String value) {
        this.workerName = value;
    }

}
