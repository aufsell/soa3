package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlRootElement(name = "request") // должен совпадать с @WebParam(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterRequest", propOrder = {"email", "password", "name"})
public class SoapRegisterRequest implements Serializable {

    @XmlElement(required = true)
    private String email;

    @XmlElement(required = true)
    private String password;

    @XmlElement(required = true)
    private String name;

    public SoapRegisterRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
