package org.lovesoa.calledadapter.soap.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private CoordinatesDto coordinates;
    private Long oscarsCount;
    private String genre;
    private String mpaaRating;
    private PersonDto operator;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate creationDate;
}
