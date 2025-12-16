package org.lovesoa.calledweb.web;


import org.lovesoa.calledejb.dtos.*;

import org.lovesoa.calledejb.service.api.MovieServiceRemote;
import org.lovesoa.calledweb.web.RemoteClient.RemoteHealthServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemoteMovieServiceClient;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

    private final RemoteMovieServiceClient client =
            new RemoteMovieServiceClient("payara-2");

    @POST
    public MovieResponseDTO createMovie(MovieCreateRequest request) throws NamingException {

        return client.createMovie(request);
    }

    @GET
    @Path("/{id}")
    public MovieResponseDTO getMovieById(@PathParam("id") Long id) throws NamingException {
        return client.getMovieById(id);
    }

    @PUT
    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request) throws NamingException {
        return client.updateMovies(request);
    }

    @PUT
    @Path("/{id}")
    public MovieResponseDTO updateMovie(@PathParam("id") Long id, MovieUpdateRequest request) throws NamingException {
        System.out.println("Updating movie " + id);
        return client.singleMovieUpdate(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) throws NamingException {
        client.deleteMovie(id);

        return Response.noContent().build();
    }

    @POST
    @Path("/search")
    public PageDTO<MovieResponseDTO> search(MovieSearchRequest request) throws NamingException {
        if (request == null) request = new MovieSearchRequest();
        return client.searchMovies(request);
    }
}
