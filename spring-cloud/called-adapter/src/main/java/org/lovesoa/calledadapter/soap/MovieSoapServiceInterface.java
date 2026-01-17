package org.lovesoa.calledadapter.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.lovesoa.calledadapter.soap.model.*;

import java.util.List;

@WebService(
    name = "MovieService",
    targetNamespace = "http://soap.calledejb.lovesoa.org/"
)
public interface MovieSoapServiceInterface {

    @WebMethod
    MovieResponseDTO createMovie(@WebParam(name = "request") MovieCreateRequest request);

    @WebMethod
    MovieResponseDTO getMovieById(@WebParam(name = "id") Long id);

    @WebMethod
    List<MovieResponseDTO> updateMovies(@WebParam(name = "request") MoviePutListDTORequest request);

    @WebMethod
    MovieResponseDTO singleMovieUpdate(
        @WebParam(name = "id") Long id,
        @WebParam(name = "request") MovieUpdateRequest request
    );

    @WebMethod
    void deleteMovie(@WebParam(name = "id") Long id);

    @WebMethod
    PageDTO searchMovies(@WebParam(name = "request") MovieSearchRequest request);
}
