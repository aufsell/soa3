package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovieSearchRequest", propOrder = {"filterName", "filterGenre", "filterMpaaRating", "filterOscarsCountMin", "filterOscarsCountMax", "sortFields", "page", "size"})
public class SoapMovieSearchRequest implements Serializable {
    
    @XmlElement
    private String filterName;
    
    @XmlElement
    private String filterGenre;
    
    @XmlElement
    private String filterMpaaRating;
    
    @XmlElement
    private Long filterOscarsCountMin;
    
    @XmlElement
    private Long filterOscarsCountMax;
    
    @XmlElement
    private List<String> sortFields;
    
    @XmlElement
    private Integer page = 0;
    
    @XmlElement
    private Integer size = 20;

    public SoapMovieSearchRequest() {}

    public String getFilterName() { return filterName; }
    public void setFilterName(String filterName) { this.filterName = filterName; }

    public String getFilterGenre() { return filterGenre; }
    public void setFilterGenre(String filterGenre) { this.filterGenre = filterGenre; }

    public String getFilterMpaaRating() { return filterMpaaRating; }
    public void setFilterMpaaRating(String filterMpaaRating) { this.filterMpaaRating = filterMpaaRating; }

    public Long getFilterOscarsCountMin() { return filterOscarsCountMin; }
    public void setFilterOscarsCountMin(Long filterOscarsCountMin) { this.filterOscarsCountMin = filterOscarsCountMin; }

    public Long getFilterOscarsCountMax() { return filterOscarsCountMax; }
    public void setFilterOscarsCountMax(Long filterOscarsCountMax) { this.filterOscarsCountMax = filterOscarsCountMax; }

    public List<String> getSortFields() { return sortFields; }
    public void setSortFields(List<String> sortFields) { this.sortFields = sortFields; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
