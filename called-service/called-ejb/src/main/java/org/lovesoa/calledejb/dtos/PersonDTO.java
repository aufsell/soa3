package org.lovesoa.calledejb.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor      // <- дефолтный конструктор
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonDTO implements Serializable {
    private Long id;
    private String name;
    private Float height;
    private Integer weight;
    private LocationDTO location;
}
