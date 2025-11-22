package org.lovesoa.calledejb.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CoordinatesDTO implements Serializable {
    private Long id;
    private Integer x;
    private Float y;
}
