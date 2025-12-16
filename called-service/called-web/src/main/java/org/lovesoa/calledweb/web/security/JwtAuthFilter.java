package org.lovesoa.calledweb.web.security;


import org.lovesoa.calledejb.security.jwt.JwtService;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    private final JwtService jwtService = new JwtService();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        System.out.println("Request path: " + path);

        if (path.startsWith("auth/") || path.equals("health") || path.equals("ping")) {
            return;
        }



        String header = requestContext.getHeaderString("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            abort(requestContext);
            return;
        }

        String token = header.substring("Bearer ".length());

        try {
            String email = jwtService.extractUsername(token);
            if (email == null || email.isBlank()) {
                abort(requestContext);
                return;
            }

            requestContext.setProperty("currentUserEmail", email);

        } catch (Exception e) {
            abort(requestContext);
        }
    }

    private void abort(ContainerRequestContext ctx) {
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
