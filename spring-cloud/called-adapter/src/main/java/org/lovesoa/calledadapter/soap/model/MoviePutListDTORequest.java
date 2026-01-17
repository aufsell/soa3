package org.lovesoa.calledadapter.soap.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MoviePutListDTORequest {
    private List<MovieCreateRequest> movies;
}
