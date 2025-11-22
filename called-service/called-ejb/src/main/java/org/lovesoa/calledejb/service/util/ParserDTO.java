package org.lovesoa.calledejb.service.util;

import org.lovesoa.calledejb.dtos.CoordinatesDTO;
import org.lovesoa.calledejb.dtos.LocationDTO;
import org.lovesoa.calledejb.dtos.MovieResponseDTO;
import org.lovesoa.calledejb.dtos.PersonDTO;
import org.lovesoa.calledejb.models.Movie;

public class ParserDTO {

    public static MovieResponseDTO MovietoDTO(Movie movie) {
        if (movie == null) return null;

        return MovieResponseDTO.builder()
                .id(movie.getId())
                .name(movie.getName())
                .coordinates(
                        CoordinatesDTO.builder()
                                .id(movie.getCoordinates().getId())
                                .x(movie.getCoordinates().getX())
                                .y(movie.getCoordinates().getY())
                                .build()
                )
                .oscarsCount(movie.getOscarsCount())
                .genre(movie.getGenre() != null ? movie.getGenre().name() : null)
                .mpaaRating(movie.getMpaaRating() != null ? movie.getMpaaRating().name() : null)
                .operator(
                        PersonDTO.builder()
                                .id(movie.getOperator().getId())
                                .name(movie.getOperator().getName())
                                .height(movie.getOperator().getHeight())
                                .weight(movie.getOperator().getWeight())
                                .location(
                                        LocationDTO.builder()
                                                .id(movie.getOperator().getLocation().getId())
                                                .x(movie.getOperator().getLocation().getX())
                                                .y(movie.getOperator().getLocation().getY())
                                                .z(movie.getOperator().getLocation().getZ())
                                                .build()
                                )
                                .build()
                )
                .creationDate(movie.getCreationDate())
                .build();
    }
}
