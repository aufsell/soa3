package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovieCreateRequest", propOrder = {"id", "name", "coordinates", "oscarsCount", "genre", "mpaaRating", "operator"})
public class SoapMovieCreateRequest implements Serializable {
    
    @XmlElement
    private Long id;
    
    @XmlElement(required = true)
    private String name;
    
    @XmlElement(required = true)
    private SoapCoordinatesInput coordinates;
    
    @XmlElement(required = true)
    private Long oscarsCount;
    
    @XmlElement
    private String genre;
    
    @XmlElement(required = true)
    private String mpaaRating;
    
    @XmlElement(required = true)
    private SoapPersonInput operator;

    public SoapMovieCreateRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SoapCoordinatesInput getCoordinates() { return coordinates; }
    public void setCoordinates(SoapCoordinatesInput coordinates) { this.coordinates = coordinates; }

    public Long getOscarsCount() { return oscarsCount; }
    public void setOscarsCount(Long oscarsCount) { this.oscarsCount = oscarsCount; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMpaaRating() { return mpaaRating; }
    public void setMpaaRating(String mpaaRating) { this.mpaaRating = mpaaRating; }

    public SoapPersonInput getOperator() { return operator; }
    public void setOperator(SoapPersonInput operator) { this.operator = operator; }

    // Inner classes for input
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "CoordinatesInput", propOrder = {"x", "y"})
    public static class SoapCoordinatesInput implements Serializable {
        @XmlElement(required = true)
        private Integer x;
        
        @XmlElement(required = true)
        private Float y;

        public SoapCoordinatesInput() {}

        public Integer getX() { return x; }
        public void setX(Integer x) { this.x = x; }

        public Float getY() { return y; }
        public void setY(Float y) { this.y = y; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "PersonInput", propOrder = {"name", "height", "weight", "location"})
    public static class SoapPersonInput implements Serializable {
        @XmlElement(required = true)
        private String name;
        
        @XmlElement(required = true)
        private Float height;
        
        @XmlElement
        private Integer weight;
        
        @XmlElement(required = true)
        private SoapLocationInput location;

        public SoapPersonInput() {}

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Float getHeight() { return height; }
        public void setHeight(Float height) { this.height = height; }

        public Integer getWeight() { return weight; }
        public void setWeight(Integer weight) { this.weight = weight; }

        public SoapLocationInput getLocation() { return location; }
        public void setLocation(SoapLocationInput location) { this.location = location; }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "LocationInput", propOrder = {"x", "y", "z"})
    public static class SoapLocationInput implements Serializable {
        @XmlElement(required = true)
        private Integer x;
        
        @XmlElement
        private int y;
        
        @XmlElement(required = true)
        private Long z;

        public SoapLocationInput() {}

        public Integer getX() { return x; }
        public void setX(Integer x) { this.x = x; }

        public int getY() { return y; }
        public void setY(int y) { this.y = y; }

        public Long getZ() { return z; }
        public void setZ(Long z) { this.z = z; }
    }
}
