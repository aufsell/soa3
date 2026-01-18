package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovieResponseDTO", propOrder = {"id", "name", "coordinates", "oscarsCount", "genre", "mpaaRating", "operator", "creationDate"})
@XmlRootElement(name = "movie")
public class SoapMovieResponseDTO implements Serializable {
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private String name;
    
    @XmlElement
    private SoapCoordinatesDTO coordinates;
    
    @XmlElement
    private Long oscarsCount;
    
    @XmlElement
    private String genre;
    
    @XmlElement
    private String mpaaRating;
    
    @XmlElement
    private SoapPersonDTO operator;
    
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate creationDate;

    public SoapMovieResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SoapCoordinatesDTO getCoordinates() { return coordinates; }
    public void setCoordinates(SoapCoordinatesDTO coordinates) { this.coordinates = coordinates; }

    public Long getOscarsCount() { return oscarsCount; }
    public void setOscarsCount(Long oscarsCount) { this.oscarsCount = oscarsCount; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getMpaaRating() { return mpaaRating; }
    public void setMpaaRating(String mpaaRating) { this.mpaaRating = mpaaRating; }

    public SoapPersonDTO getOperator() { return operator; }
    public void setOperator(SoapPersonDTO operator) { this.operator = operator; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
}
