//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.09 at 04:23:54 PM GMT 
//


package uk.gov.ofwat.fountain.modelbuilder.model.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;all&gt;
 *         &lt;element ref="{http://www.ofwat.gov.uk/model2}sectiondetails"/&gt;
 *         &lt;element ref="{http://www.ofwat.gov.uk/model2}forms" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.ofwat.gov.uk/model2}documents" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.ofwat.gov.uk/model2}lines" minOccurs="0"/&gt;
 *       &lt;/all&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "section")
public class Section {

    @XmlElement(required = true)
    protected Sectiondetails sectiondetails;
    protected Forms forms;
    protected Documents documents;
    protected Lines lines;

    /**
     * Gets the value of the sectiondetails property.
     * 
     * @return
     *     possible object is
     *     {@link Sectiondetails }
     *     
     */
    public Sectiondetails getSectiondetails() {
        return sectiondetails;
    }

    /**
     * Sets the value of the sectiondetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sectiondetails }
     *     
     */
    public void setSectiondetails(Sectiondetails value) {
        this.sectiondetails = value;
    }

    /**
     * Gets the value of the forms property.
     * 
     * @return
     *     possible object is
     *     {@link Forms }
     *     
     */
    public Forms getForms() {
        return forms;
    }

    /**
     * Sets the value of the forms property.
     * 
     * @param value
     *     allowed object is
     *     {@link Forms }
     *     
     */
    public void setForms(Forms value) {
        this.forms = value;
    }

    /**
     * Gets the value of the documents property.
     * 
     * @return
     *     possible object is
     *     {@link Documents }
     *     
     */
    public Documents getDocuments() {
        return documents;
    }

    /**
     * Sets the value of the documents property.
     * 
     * @param value
     *     allowed object is
     *     {@link Documents }
     *     
     */
    public void setDocuments(Documents value) {
        this.documents = value;
    }

    /**
     * Gets the value of the lines property.
     * 
     * @return
     *     possible object is
     *     {@link Lines }
     *     
     */
    public Lines getLines() {
        return lines;
    }

    /**
     * Sets the value of the lines property.
     * 
     * @param value
     *     allowed object is
     *     {@link Lines }
     *     
     */
    public void setLines(Lines value) {
        this.lines = value;
    }

}
