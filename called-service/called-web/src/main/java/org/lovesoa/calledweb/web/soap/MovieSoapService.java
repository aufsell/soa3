package org.lovesoa.calledweb.web.soap;

import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledweb.web.RemoteClient.RemoteAuthServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemoteMovieServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemotePingServiceClient;
import org.lovesoa.calledweb.web.soap.dto.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.naming.NamingException;
import java.util.List;

/**
 * SOAP Web Service for Movie operations.
 * This service calls EJB beans remotely through RemoteClient classes.
 */
@WebService(
        name = "MovieService",
        serviceName = "MovieWebService",
        targetNamespace = "http://soap.web.calledweb.lovesoa.org/"
)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class MovieSoapService {

    private final RemoteMovieServiceClient movieClient = new RemoteMovieServiceClient("payara-2");
    private final RemoteAuthServiceClient authClient = new RemoteAuthServiceClient("payara-2");
    private final RemotePingServiceClient pingClient = new RemotePingServiceClient("payara-2");

    // ========== Movie Operations ==========

    @WebMethod(operationName = "createMovie")
    @WebResult(name = "movie")
    public SoapMovieResponseDTO createMovie(
            @WebParam(name = "request") SoapMovieCreateRequest request
    ) throws SoapServiceException {
        try {
            MovieCreateRequest internal = SoapDtoConverter.toMovieCreateRequest(request);
            MovieResponseDTO result = movieClient.createMovie(internal);
            return SoapDtoConverter.toSoapMovieResponseDTO(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to create movie: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "getMovieById")
    @WebResult(name = "movie")
    public SoapMovieResponseDTO getMovieById(
            @WebParam(name = "id") Long id
    ) throws SoapServiceException {
        try {
            MovieResponseDTO result = movieClient.getMovieById(id);
            return SoapDtoConverter.toSoapMovieResponseDTO(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to get movie: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "updateMovie")
    @WebResult(name = "movie")
    public SoapMovieResponseDTO updateMovie(
            @WebParam(name = "id") Long id,
            @WebParam(name = "request") SoapMovieUpdateRequest request
    ) throws SoapServiceException {
        try {
            MovieUpdateRequest internal = SoapDtoConverter.toMovieUpdateRequest(request);
            MovieResponseDTO result = movieClient.singleMovieUpdate(id, internal);
            return SoapDtoConverter.toSoapMovieResponseDTO(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to update movie: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "updateMovies")
    @WebResult(name = "movies")
    public List<SoapMovieResponseDTO> updateMovies(
            @WebParam(name = "request") SoapMoviePutListRequest request
    ) throws SoapServiceException {
        try {
            MoviePutListDTORequest internal = SoapDtoConverter.toMoviePutListRequest(request);
            List<MovieResponseDTO> result = movieClient.updateMovies(internal);
            return SoapDtoConverter.toSoapMovieResponseDTOList(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to update movies: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "deleteMovie")
    public void deleteMovie(
            @WebParam(name = "id") Long id
    ) throws SoapServiceException {
        try {
            movieClient.deleteMovie(id);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to delete movie: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "searchMovies")
    @WebResult(name = "pageResult")
    public SoapPageDTO searchMovies(
            @WebParam(name = "request") SoapMovieSearchRequest request
    ) throws SoapServiceException {
        try {
            MovieSearchRequest internal = SoapDtoConverter.toMovieSearchRequest(request);
            PageDTO<MovieResponseDTO> result = movieClient.searchMovies(internal);
            return SoapDtoConverter.toSoapPageDTO(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to search movies: " + e.getMessage(), e);
        }
    }

    // ========== Auth Operations ==========

    @WebMethod(operationName = "login")
    @WebResult(name = "authResponse")
    public SoapAuthResponse login(
            @WebParam(name = "request") SoapLoginRequest request
    ) throws SoapServiceException {
        try {
            LoginRequest internal = SoapDtoConverter.toLoginRequest(request);
            AuthResponse result = authClient.login(internal);
            return SoapDtoConverter.toSoapAuthResponse(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to login: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "register")
    @WebResult(name = "authResponse")
    public SoapAuthResponse register(
            @WebParam(name = "request") SoapRegisterRequest request
    ) throws SoapServiceException {
        try {
            RegisterRequest internal = SoapDtoConverter.toRegisterRequest(request);
            System.out.println("REquest: " + request.getEmail() + " "+ request.getName() + " " + request.getPassword());
            System.out.println("converterDTO: " +internal.getEmail() + " " + internal.getName() + " " + internal.getPassword());
            RemoteAuthServiceClient client = new RemoteAuthServiceClient("payara-2");
            AuthResponse result = client.register(internal);
            return SoapDtoConverter.toSoapAuthResponse(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to register: " + e.getMessage(), e);
        }
    }

    // ========== Health Operations ==========

    @WebMethod(operationName = "ping")
    @WebResult(name = "result")
    public String ping() throws SoapServiceException {
        try {
            return pingClient.ping();
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to ping: " + e.getMessage(), e);
        }
    }
}
