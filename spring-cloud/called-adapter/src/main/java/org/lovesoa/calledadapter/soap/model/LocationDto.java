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
public class LocationDto {
    private Integer x;
    private int y;
    private Long z;
}
