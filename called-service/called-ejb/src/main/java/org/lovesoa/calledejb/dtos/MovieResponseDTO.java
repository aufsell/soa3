package org.lovesoa.calledejb.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lovesoa.calledejb.models.Coordinates;
import org.lovesoa.calledejb.models.Person;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieResponseDTO implements Serializable {
    private Long id;
    private String name;
    private CoordinatesDTO coordinates;
    private Long oscarsCount;
    private String genre;
    private String mpaaRating;
    private PersonDTO operator;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate creationDate;
}
