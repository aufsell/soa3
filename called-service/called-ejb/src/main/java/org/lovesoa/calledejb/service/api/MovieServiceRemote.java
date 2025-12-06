package org.lovesoa.calledejb.service.api;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ejb.Remote;
import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.models.Movie;

import java.util.List;

@Remote
public interface MovieServiceRemote {
    public MovieResponseDTO createMovie(MovieCreateRequest request);
    public MovieResponseDTO getMovieById(Long id);
    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request);
    public MovieResponseDTO singleMovieUpdate(Long id, MovieUpdateRequest request);
    public void deleteMovie(Long id);
    public PageDTO<MovieResponseDTO> searchMovies(MovieSearchRequest request);
}