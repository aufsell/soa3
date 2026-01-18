package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovieUpdateRequest", propOrder = {"name", "coordinates", "oscarsCount", "genre", "mpaaRating", "operator"})
public class SoapMovieUpdateRequest implements Serializable {
    
    @XmlElement(required = true)
    private String name;
    
    @XmlElement(required = true)
    private SoapMovieCreateRequest.SoapCoordinatesInput coordinates;
    
    @XmlElement(required = true)
    private Long oscarsCount;
    
    @XmlElement
    private String genre;
    
    @XmlElement(required = true)
    private String mpaaRating;
    
    @XmlElement(required = true)
    private SoapMovieCreateRequest.SoapPersonInput operator;

    public SoapMovieUpdateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SoapMovieCreateRequest.SoapCoordinatesInput getCoordinates() { return coordinates; }
    public void setCoordinates(SoapMovieCreateRequest.SoapCoordinatesInput coordinates) { this.coordinates = coordinates; }

    public Long getOscarsCount() { return oscarsCount; }
    public void setOscarsCount(Long oscarsCount) { this.oscarsCount = oscarsCount; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMpaaRating() { return mpaaRating; }
    public void setMpaaRating(String mpaaRating) { this.mpaaRating = mpaaRating; }

    public SoapMovieCreateRequest.SoapPersonInput getOperator() { return operator; }
    public void setOperator(SoapMovieCreateRequest.SoapPersonInput operator) { this.operator = operator; }
}
