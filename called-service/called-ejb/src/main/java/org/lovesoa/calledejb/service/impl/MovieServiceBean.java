package org.lovesoa.calledejb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.models.*;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.lovesoa.calledejb.service.util.ParserDTO.MovietoDTO;

@Stateless
public class MovieServiceBean  implements MovieServiceRemote {

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Transactional
    public MovieResponseDTO createMovie(MovieCreateRequest request){

        if (request.getId() < 1) {
            throw new BadRequestException("id must be >= 1");
        }

        Location location = Location.builder()
                .x(request.getOperator().getLocation().getX())
                .y(request.getOperator().getLocation().getY())
                .z(request.getOperator().getLocation().getZ())
                .build();
        em.persist(location);

        Person operator = Person.builder()
                .name(request.getOperator().getName())
                .height(request.getOperator().getHeight())
                .weight(request.getOperator().getWeight())
                .location(location)
                .build();
        em.persist(operator);

        Coordinates coordinates = Coordinates.builder()
                .x(request.getCoordinates().getX())
                .y(request.getCoordinates().getY())
                .build();
        em.persist(coordinates);

        Movie movie = Movie.builder()
                .name(request.getName())
                .coordinates(coordinates)
                .oscarsCount(Long.valueOf(request.getOscarsCount()))
                .genre(request.getGenre() != null ? MovieGenre.valueOf(request.getGenre()) : null)
                .mpaaRating(MpaaRating.valueOf(request.getMpaaRating()))
                .operator(operator)
                .creationDate(LocalDate.now())
                .build();

        em.persist(movie);
        return MovietoDTO(movie);
    }

    public Movie getMovie(Long id){
        if (id == null || id < 1) {
            throw new BadRequestException("id must be >= 1");
        }

        Movie movie = em.find(Movie.class, id);

        if (movie == null) {
            throw new NullPointerException("movie with id " + id + " not found");
        }
        return movie;
    }

    public MovieResponseDTO getMovieById(Long id){
        Movie movie = getMovie(id);

        return MovietoDTO(movie);
    }

    @Transactional
    public Movie updateMovie(Long id, MovieCreateRequest request){

        Movie movie = getMovie(id);

        if (request.getOperator() != null && request.getOperator().getLocation() != null) {
            Location location = movie.getOperator().getLocation();
            if (location == null) {
                location = Location.builder().build();
                movie.getOperator().setLocation(location);
            }
            location.setX(request.getOperator().getLocation().getX());
            location.setY(request.getOperator().getLocation().getY());
            location.setZ(request.getOperator().getLocation().getZ());
            em.persist(location);
        }

        if (request.getOperator() != null) {
            Person operator = movie.getOperator();
            if (operator == null) {
                operator = Person.builder().build();
                movie.setOperator(operator);
            }
            operator.setName(request.getOperator().getName());
            operator.setHeight(request.getOperator().getHeight());
            operator.setWeight(request.getOperator().getWeight());
            operator.setLocation(movie.getOperator().getLocation());
            em.persist(operator);
        }

        if (request.getCoordinates() != null) {
            Coordinates coordinates = movie.getCoordinates();
            if (coordinates == null) {
                coordinates = Coordinates.builder().build();
                movie.setCoordinates(coordinates);
            }
            coordinates.setX(request.getCoordinates().getX());
            coordinates.setY(request.getCoordinates().getY());
            em.persist(coordinates);
        }

        movie.setName(request.getName());
        movie.setOscarsCount(request.getOscarsCount());
        movie.setGenre(request.getGenre() != null ? MovieGenre.valueOf(request.getGenre()) : null);
        movie.setMpaaRating(MpaaRating.valueOf(request.getMpaaRating()));
        em.persist(movie);
        return movie;

    }

    @Transactional
    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request) {

        if (request.getMovies() == null || request.getMovies().isEmpty()) {
            throw new BadRequestException("movies array cannot be empty");
        }

        List<MovieResponseDTO> result = new ArrayList<>();

        for (MovieCreateRequest req : request.getMovies()) {

            if (req.getId() == null || req.getId() < 1) {
                throw new BadRequestException("Each movie must contain id >= 1");
            }

            Movie updated = updateMovie(req.getId(), req);
            result.add(MovietoDTO(updated));
        }

        return result;
    }


    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = getMovie(id);
        if (movie == null) {
            throw new NotFoundException("Movie with id " + id + " not found");
        }

        em.remove(movie);
    }
}
