package org.lovesoa.calledejb.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordinates_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Coordinates coordinates;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @PositiveOrZero
    private Long oscarsCount;

    @Enumerated(EnumType.STRING)
    private MovieGenre genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MpaaRating mpaaRating;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "operator_id", referencedColumnName = "id")
    private Person operator;
}
