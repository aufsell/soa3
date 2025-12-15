package org.lovesoa.calledweb.web;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.models.Movie;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;
import java.util.Properties;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {
//    @EJB(lookup="corbname:iiop:payara-2:3700#" +"java:global/called-ejb/MovieServiceBean!org.lovesoa.calledejb.service.api.MovieServiceRemote")
    private MovieServiceRemote movieService;

    public MovieResource() {
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put(Context.PROVIDER_URL, "iiop://payara-2:3700");


            Context ctx = new InitialContext(props);
            movieService = (MovieServiceRemote) ctx.lookup(
                    "java:global/called-ejb/MovieServiceBean!org.lovesoa.calledejb.service.api.MovieServiceRemote"
            );

        } catch (Exception e) {
            throw new RuntimeException("Cannot lookup remote AuthService", e);
        }
    }

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

    @PUT
    @Path("/{id}")
    public MovieResponseDTO updateMovie(@PathParam("id") Long id, MovieUpdateRequest request) {
        System.out.println("Updating movie " + id);
        return movieService.singleMovieUpdate(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        movieService.deleteMovie(id);

        return Response.noContent().build();
    }

    @POST
    @Path("/search")
    public PageDTO<MovieResponseDTO> search(MovieSearchRequest request) {
        if (request == null) request = new MovieSearchRequest();
        return movieService.searchMovies(request);
    }
}
