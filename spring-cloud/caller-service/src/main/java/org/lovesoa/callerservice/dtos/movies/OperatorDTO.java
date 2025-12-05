package org.lovesoa.callerservice.dtos.movies;

import lombok.Data;

@Data
public class OperatorDTO {
    private Long id;
    private String name;
    private Double height;
    private Double weight;
    private LocationDTO location;
}
