package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MoviePutListRequest", propOrder = {"movies"})
public class SoapMoviePutListRequest implements Serializable {
    
    @XmlElement
    private List<SoapMovieCreateRequest> movies;

    public SoapMoviePutListRequest() {}

    public List<SoapMovieCreateRequest> getMovies() { return movies; }
    public void setMovies(List<SoapMovieCreateRequest> movies) { this.movies = movies; }
}
