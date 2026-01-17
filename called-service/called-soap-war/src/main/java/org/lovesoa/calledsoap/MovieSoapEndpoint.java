package org.lovesoa.calledsoap;

import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

@WebService(
    name = "MovieService",
    serviceName = "MovieService",
    targetNamespace = "http://soap.calledejb.lovesoa.org/",
    portName = "MovieServicePort"
)
public class MovieSoapEndpoint {

    private MovieServiceRemote getMovieService() {
        try {
            InitialContext ctx = new InitialContext();
            return (MovieServiceRemote) ctx.lookup("java:global/called-ejb/MovieServiceBean!org.lovesoa.calledejb.service.api.MovieServiceRemote");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup MovieService EJB", e);
        }
    }

    @WebMethod
    public MovieResponseDTO createMovie(@WebParam(name = "request") MovieCreateRequest request) {
        return getMovieService().createMovie(request);
    }

    @WebMethod
    public MovieResponseDTO getMovieById(@WebParam(name = "id") Long id) {
        return getMovieService().getMovieById(id);
    }

    @WebMethod
    public List<MovieResponseDTO> updateMovies(@WebParam(name = "request") MoviePutListDTORequest request) {
        return getMovieService().updateMovies(request);
    }

    @WebMethod
    public MovieResponseDTO singleMovieUpdate(
        @WebParam(name = "id") Long id,
        @WebParam(name = "request") MovieUpdateRequest request
    ) {
        return getMovieService().singleMovieUpdate(id, request);
    }

    @WebMethod
    public void deleteMovie(@WebParam(name = "id") Long id) {
        getMovieService().deleteMovie(id);
    }

    @WebMethod
    public PageDTO searchMovies(@WebParam(name = "request") MovieSearchRequest request) {
        return getMovieService().searchMovies(request);
    }
}
