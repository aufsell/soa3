package org.lovesoa.calledadapter.soap.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonDto {
    private String name;
    private Float height;
    private Integer weight;
    private LocationDto location;
}
