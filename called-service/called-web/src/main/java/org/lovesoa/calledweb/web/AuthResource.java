package org.lovesoa.calledweb.web;

import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lovesoa.calledejb.dtos.RegisterRequest;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;
import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private AuthServiceRemote authService;

    public AuthResource() {
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put(Context.PROVIDER_URL, "iiop://payara-2:3700");


            Context ctx = new InitialContext(props);
            authService = (AuthServiceRemote) ctx.lookup(
                    "java:global/called-ejb/AuthServiceBean!org.lovesoa.calledejb.service.api.AuthServiceRemote"
            );

        } catch (Exception e) {
            throw new RuntimeException("Cannot lookup remote AuthService", e);
        }
    }

//    @EJB(lookup="corbname:iiop:payara-2:3700#" +"java:global/called-ejb/AuthServiceBean!org.lovesoa.calledejb.service.api.AuthServiceRemote")

    @POST
    @Path("/register")
    public AuthResponse register(RegisterRequest request) {
        return authService.register(request);
    }

    @POST
    @Path("/login")
    public AuthResponse login(LoginRequest request) {
        return authService.login(request);
    }
}

