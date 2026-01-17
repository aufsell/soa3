package org.lovesoa.calledejb.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MoviePutListDTORequest {
    private List<MovieCreateRequest> movies;
}
