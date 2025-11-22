package org.lovesoa.calledejb.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PersonDTO implements Serializable {
    private Long id;
    private String name;
    private Float height;
    private Integer weight;
    private LocationDTO location;
}
