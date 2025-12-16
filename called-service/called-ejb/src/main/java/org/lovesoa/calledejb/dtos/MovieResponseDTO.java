package org.lovesoa.calledejb.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lovesoa.calledejb.models.Coordinates;
import org.lovesoa.calledejb.models.Person;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseDTO implements Serializable {
    private Long id;
    private String name;
    private CoordinatesDTO coordinates;
    private Long oscarsCount;
    private String genre;
    private String mpaaRating;
    private PersonDTO operator;
    private LocalDate creationDate;
}
