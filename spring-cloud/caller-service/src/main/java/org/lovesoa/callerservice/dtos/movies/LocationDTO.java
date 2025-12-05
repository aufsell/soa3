package org.lovesoa.callerservice.dtos.movies;

import lombok.Data;

@Data
public class LocationDTO {
    private Long id;
    private Integer x;
    private Integer y;
    private Long z;
}
