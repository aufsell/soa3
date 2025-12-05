package org.lovesoa.callerservice.controller;

import com.google.common.net.HttpHeaders;
import org.lovesoa.callerservice.MoviesClient;
import org.lovesoa.callerservice.dtos.RedistributeRewardsResponseDTO;
import org.lovesoa.callerservice.dtos.movies.MovieResponseDTO;
import org.lovesoa.callerservice.dtos.movies.MovieSearchRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/caller")
public class GenresController {

//    private final CalledAdapterClient adapterClient;
//
//    public GenresController(CalledAdapterClient adapterClient) {
//        this.adapterClient = adapterClient;
//    }

    @GetMapping("/ping")
    public String ping() {
        return "pong from caller-service";
    }

    private final MoviesClient moviesClient;

    public GenresController(MoviesClient moviesClient) {
        this.moviesClient = moviesClient;
    }

//    @PostMapping("/testGenre/{genre}")
//    public RedistributeRewardsResponseDTO redistributeRewards(
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
//            @PathVariable String genre
//    ) {
//
//    }


    @PostMapping("/redistribute-rewards/{fromGenre}/{toGenre}")
    public RedistributeRewardsResponseDTO redistributeRewards(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
            @PathVariable String fromGenre,
            @PathVariable String toGenre
    ) {
        if (bearerToken == null || bearerToken.isBlank()) {
            throw new IllegalArgumentException("Bearer-токен обязателен");
        }

        String token = bearerToken.replace("Bearer ", "");

        MovieSearchRequest fromRequest = new MovieSearchRequest();
        fromRequest.setFilters(Map.of(
                "genre[eq]", fromGenre,
                "oscarsCount[gt]", 0
        ));
        List<MovieResponseDTO> fromMovies = moviesClient.searchMovies(fromRequest, token).getContent();

        MovieSearchRequest toRequest = new MovieSearchRequest();
        toRequest.setFilters(Map.of("genre[eq]", toGenre));
        List<MovieResponseDTO> toMovies = moviesClient.searchMovies(toRequest, token).getContent();

        System.out.println("Отправили и распарсили 2 селекта");
        int totalOscars = fromMovies.stream().mapToInt(MovieResponseDTO::getOscarsCount).sum();

        // Обнуляем все и объединяем списки в один
        List<MovieResponseDTO> allMovies = new ArrayList<>();
        fromMovies.forEach(m -> m.setOscarsCount(0));
        allMovies.addAll(fromMovies);

        Random random = new Random();
        for (int i = 0; i < totalOscars; i++) {
            if (toMovies.isEmpty()) break;
            MovieResponseDTO randomMovie = toMovies.get(random.nextInt(toMovies.size()));
            randomMovie.setOscarsCount(randomMovie.getOscarsCount() + 1);
        }
        allMovies.addAll(toMovies);
        
        moviesClient.updateMoviesBatch(allMovies, token);

        RedistributeRewardsResponseDTO response = new RedistributeRewardsResponseDTO();
        response.setTransferredCount(totalOscars);
        return response;
    }


}
