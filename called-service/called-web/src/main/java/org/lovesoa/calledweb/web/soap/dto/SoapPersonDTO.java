package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonDTO", propOrder = {"id", "name", "height", "weight", "location"})
public class SoapPersonDTO implements Serializable {
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private String name;
    
    @XmlElement
    private Float height;
    
    @XmlElement
    private Integer weight;
    
    @XmlElement
    private SoapLocationDTO location;

    public SoapPersonDTO() {}

    public SoapPersonDTO(Long id, String name, Float height, Integer weight, SoapLocationDTO location) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.location = location;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Float getHeight() { return height; }
    public void setHeight(Float height) { this.height = height; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public SoapLocationDTO getLocation() { return location; }
    public void setLocation(SoapLocationDTO location) { this.location = location; }
}
