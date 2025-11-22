package org.lovesoa.calledejb.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LocationDTO implements Serializable {
    private Long id;
    private Integer x;
    private Integer y;
    private Long z;
}
