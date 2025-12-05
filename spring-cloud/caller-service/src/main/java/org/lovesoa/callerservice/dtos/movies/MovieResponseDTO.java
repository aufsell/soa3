package org.lovesoa.callerservice.dtos.movies;

import lombok.Data;

@Data
public class MovieResponseDTO {
    private Long id;
    private String name;
    private CoordinatesDTO coordinates; // новое поле
    private String creationDate;         // новое поле
    private int oscarsCount;
    private String genre;
    private String mpaaRating;           // новое поле
    private OperatorDTO operator;
}

