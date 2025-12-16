package org.lovesoa.calledweb.web;


import org.lovesoa.calledejb.dtos.RegisterRequest;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;
import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledweb.web.RemoteClient.RemoteAuthServiceClient;
import org.lovesoa.calledweb.web.RemoteClient.RemoteHealthServiceClient;


import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final RemoteAuthServiceClient client =
            new RemoteAuthServiceClient("payara-2");

    @POST
    @Path("/register")
    public AuthResponse register(RegisterRequest request) throws NamingException {
        return client.register(request);
    }

    @POST
    @Path("/login")
    public AuthResponse login(LoginRequest request) throws NamingException {
        return client.login(request);
    }
}

