package org.lovesoa.calledejb.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDTO implements Serializable {
    private Long id;
    private Integer x;
    private Float y;
}
