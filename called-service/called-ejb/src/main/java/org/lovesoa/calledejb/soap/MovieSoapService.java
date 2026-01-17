package org.lovesoa.calledejb.soap;

import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ejb.Stateless;
import java.util.List;

@WebService(
    name = "MovieService",
    serviceName = "MovieService",
    targetNamespace = "http://soap.calledejb.lovesoa.org/"
)
@Stateless
public class MovieSoapService {

    @EJB
    private MovieServiceRemote movieService;

    @WebMethod
    public MovieResponseDTO createMovie(@WebParam(name = "request") MovieCreateRequest request) {
        return movieService.createMovie(request);
    }

    @WebMethod
    public MovieResponseDTO getMovieById(@WebParam(name = "id") Long id) {
        return movieService.getMovieById(id);
    }

    @WebMethod
    public List<MovieResponseDTO> updateMovies(@WebParam(name = "request") MoviePutListDTORequest request) {
        return movieService.updateMovies(request);
    }

    @WebMethod
    public MovieResponseDTO singleMovieUpdate(
        @WebParam(name = "id") Long id,
        @WebParam(name = "request") MovieUpdateRequest request
    ) {
        return movieService.singleMovieUpdate(id, request);
    }

    @WebMethod
    public void deleteMovie(@WebParam(name = "id") Long id) {
        movieService.deleteMovie(id);
    }

    @WebMethod
    public PageDTO searchMovies(@WebParam(name = "request") MovieSearchRequest request) {
        return movieService.searchMovies(request);
    }
}
