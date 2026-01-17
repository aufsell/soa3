package org.lovesoa.calledadapter.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieUpdateRequest {

    private String name;
    private CoordinatesDTO coordinates;
    private Long oscarsCount;
    private String genre;
    private String mpaaRating;
    private PersonDTO operator;
}
