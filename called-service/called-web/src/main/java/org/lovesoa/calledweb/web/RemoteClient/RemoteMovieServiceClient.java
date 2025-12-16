package org.lovesoa.calledweb.web.RemoteClient;

import fish.payara.ejb.http.client.RemoteEJBContextFactory;
import org.lovesoa.calledejb.dtos.*;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;
import org.lovesoa.calledejb.service.api.MovieServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Properties;

public class RemoteMovieServiceClient {
    private final String ejbHost;

    public RemoteMovieServiceClient(String ejbHost) {
        this.ejbHost = ejbHost;
    }

    protected MovieServiceRemote lookupRemoteBean() throws NamingException {
        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, RemoteEJBContextFactory.class.getName());

        props.put(Context.PROVIDER_URL, "http://" + ejbHost + ":8080/ejb-invoker");

        Context ctx = new InitialContext(props);

        String jndiName = "java:global/called-ejb/MovieServiceBean!org.lovesoa.calledejb.service.api.MovieServiceRemote";

        return (MovieServiceRemote) ctx.lookup(jndiName);
    }


    public MovieResponseDTO createMovie(MovieCreateRequest request) throws NamingException {
        MovieServiceRemote remote = lookupRemoteBean();
        return remote.createMovie(request);
    }

    public MovieResponseDTO getMovieById(Long id) throws NamingException {
        MovieServiceRemote remote = lookupRemoteBean();
        return remote.getMovieById(id);
    }

    public List<MovieResponseDTO> updateMovies(MoviePutListDTORequest request) throws NamingException {
        MovieServiceRemote remote = lookupRemoteBean();
        return remote.updateMovies(request);
    }

    public MovieResponseDTO singleMovieUpdate(Long id, MovieUpdateRequest request) throws NamingException {
        MovieServiceRemote remote = lookupRemoteBean();
        return remote.singleMovieUpdate(id, request);
    }

    public void deleteMovie(Long id) throws NamingException {
        MovieServiceRemote remote = lookupRemoteBean();
        remote.deleteMovie(id);
    }

    public PageDTO<MovieResponseDTO> searchMovies(MovieSearchRequest request) throws NamingException {
        MovieServiceRemote remote = lookupRemoteBean();
        return remote.searchMovies(request);
    }
}
