package org.lovesoa.calledadapter.soap.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieCreateRequest {
    private Long id;
    private String name;
    private CoordinatesDto coordinates;
    private Long oscarsCount;
    private String genre;
    private String mpaaRating;
    private PersonDto operator;
}
