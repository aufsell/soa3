package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HealthResponse", propOrder = {"status", "database", "ejb"})
@XmlRootElement(name = "healthResponse")
public class SoapHealthResponse implements Serializable {

    @XmlElement(required = true)
    private String status;

    @XmlElement(required = true)
    private String database;

    @XmlElement(required = true)
    private String ejb;

    public SoapHealthResponse() {}

    public SoapHealthResponse(String status, String database, String ejb) {
        this.status = status;
        this.database = database;
        this.ejb = ejb;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String getEjb() { return ejb; }
    public void setEjb(String ejb) { this.ejb = ejb; }
}
