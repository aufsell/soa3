package org.lovesoa.calledejb.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor      // <- дефолтный конструктор
@AllArgsConstructor
public class PersonDTO implements Serializable {
    private Long id;
    private String name;
    private Float height;
    private Integer weight;
    private LocationDTO location;
}
