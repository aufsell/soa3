package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoordinatesDTO", propOrder = {"id", "x", "y"})
public class SoapCoordinatesDTO implements Serializable {
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private Integer x;
    
    @XmlElement
    private Float y;

    public SoapCoordinatesDTO() {}

    public SoapCoordinatesDTO(Long id, Integer x, Float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }

    public Float getY() { return y; }
    public void setY(Float y) { this.y = y; }
}
