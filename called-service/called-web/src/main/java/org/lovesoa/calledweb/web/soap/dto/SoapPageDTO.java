package org.lovesoa.calledweb.web.soap.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PageDTO", propOrder = {"content", "page", "size", "totalElements", "totalPages"})
@XmlRootElement(name = "pageResult")
public class SoapPageDTO implements Serializable {
    
    @XmlElement
    private List<SoapMovieResponseDTO> content;
    
    @XmlElement
    private int page;
    
    @XmlElement
    private int size;
    
    @XmlElement
    private long totalElements;
    
    @XmlElement
    private int totalPages;

    public SoapPageDTO() {}

    public List<SoapMovieResponseDTO> getContent() { return content; }
    public void setContent(List<SoapMovieResponseDTO> content) { this.content = content; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
