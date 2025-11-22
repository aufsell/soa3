package org.lovesoa.calledweb.web;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lovesoa.calledejb.dtos.MovieCreateRequest;
import org.lovesoa.calledejb.dtos.MoviePutListDTORequest;
import org.lovesoa.calledejb.dtos.MovieResponseDTO;
import org.lovesoa.calledejb.models.Movie;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;

import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {
    @EJB
    private MovieServiceRemote movieService;

    @POST
    public MovieResponseDTO createMovie(MovieCreateRequest request) {

        return movieService.createMovie(request);
    }

    @GET
    @Path("/{id}")
    public MovieResponseDTO getMovieById(@PathParam("id") Long id) {
        return movieService.getMovieById(id);
    }

    @PUT
    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request) {
        return movieService.updateMovies(request);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        movieService.deleteMovie(id);

        return Response.noContent().build(); // HTTP 204 No Content
    }
}
