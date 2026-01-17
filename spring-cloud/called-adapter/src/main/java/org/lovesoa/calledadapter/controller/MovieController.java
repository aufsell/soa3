package org.lovesoa.calledadapter.controller;

import org.lovesoa.calledadapter.dto.*;
import org.lovesoa.calledadapter.soap.MovieSoapClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieSoapClient movieSoapClient;

    public MovieController(MovieSoapClient movieSoapClient) {
        this.movieSoapClient = movieSoapClient;
    }

    @PostMapping
    public ResponseEntity<MovieResponseDTO> createMovie(@RequestBody MovieCreateRequest request) {
        MovieResponseDTO response = movieSoapClient.createMovie(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> getMovieById(@PathVariable Long id) {
        MovieResponseDTO response = movieSoapClient.getMovieById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<List<MovieResponseDTO>> updateMovies(@RequestBody MoviePutListDTORequest request) {
        List<MovieResponseDTO> response = movieSoapClient.updateMovies(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieResponseDTO> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieUpdateRequest request
    ) {
        MovieResponseDTO response = movieSoapClient.singleMovieUpdate(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieSoapClient.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<PageDTO<MovieResponseDTO>> search(@RequestBody MovieSearchRequest request) {
        if (request == null) request = new MovieSearchRequest();
        PageDTO<MovieResponseDTO> response = movieSoapClient.searchMovies(request);
        return ResponseEntity.ok(response);
    }
}
