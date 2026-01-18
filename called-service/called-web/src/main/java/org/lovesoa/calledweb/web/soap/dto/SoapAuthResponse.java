package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AuthResponse", propOrder = {"token"})
@XmlRootElement(name = "authResponse")
public class SoapAuthResponse implements Serializable {
    
    @XmlElement
    private String token;

    public SoapAuthResponse() {}

    public SoapAuthResponse(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
