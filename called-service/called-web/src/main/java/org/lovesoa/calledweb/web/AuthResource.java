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

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @EJB
    private AuthServiceRemote authService;

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

