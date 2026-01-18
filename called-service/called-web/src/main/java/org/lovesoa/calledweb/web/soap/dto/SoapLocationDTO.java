package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocationDTO", propOrder = {"id", "x", "y", "z"})
public class SoapLocationDTO implements Serializable {
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private Integer x;
    
    @XmlElement
    private Integer y;
    
    @XmlElement
    private Long z;

    public SoapLocationDTO() {}

    public SoapLocationDTO(Long id, Integer x, Integer y, Long z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getX() { return x; }
    public void setX(Integer x) { this.x = x; }

    public Integer getY() { return y; }
    public void setY(Integer y) { this.y = y; }

    public Long getZ() { return z; }
    public void setZ(Long z) { this.z = z; }
}
