package org.lovesoa.calledweb.web.soap;

import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledweb.web.RemoteClient.RemoteAuthServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemoteHealthServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemoteMovieServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemotePingServiceClient;
import org.lovesoa.calledweb.web.soap.dto.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


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
    private final RemoteHealthServiceClient healthClient = new RemoteHealthServiceClient("payara-2");

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;



    @WebMethod(operationName = "createMovie")
    @WebResult(name = "movie")
    public SoapMovieResponseDTO createMovie(
            @WebParam(name = "request") SoapMovieCreateRequest request
    ) throws SoapServiceException {
        System.out.println("soap create Movie");
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
        System.out.println("soap get Movie");
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
        System.out.println("soap update Movie");
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
        System.out.println("soap update Movies");
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
        System.out.println("soap delete Movie");
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
        System.out.println("soap search");
        try {
            MovieSearchRequest internal = SoapDtoConverter.toMovieSearchRequest(request);
            PageDTO<MovieResponseDTO> result = movieClient.searchMovies(internal);
            return SoapDtoConverter.toSoapPageDTO(result);
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to search movies: " + e.getMessage(), e);
        }
    }


    @WebMethod(operationName = "login")
    @WebResult(name = "authResponse")
    public SoapAuthResponse login(
            @WebParam(name = "request") SoapLoginRequest request
    ) throws SoapServiceException {
        System.out.println("soap login");
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
        System.out.println("soap register");
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


    @WebMethod(operationName = "ping")
    @WebResult(name = "result")
    public String ping() throws SoapServiceException {
        System.out.println("soap ping");
        try {
            return pingClient.ping();
        } catch (NamingException e) {
            throw new SoapServiceException("Failed to ping: " + e.getMessage(), e);
        }
    }

    @WebMethod(operationName = "health")
    @WebResult(name = "healthResponse")
    public SoapHealthResponse health() {
        System.out.println("soap healthcheck");
        boolean dbOk = checkDb();
        boolean ejbOk = checkEjb();
        boolean up = dbOk && ejbOk;

        return new SoapHealthResponse(
                up ? "UP" : "DOWN",
                dbOk ? "UP" : "DOWN",
                ejbOk ? "UP" : "DOWN"
        );
    }

    private boolean checkDb() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkEjb() {
        try {
            healthClient.healthCheck();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
