package org.lovesoa.calledweb.web.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.lovesoa.calledejb.security.jwt.JwtService;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {

    private final JwtService jwtService = new JwtService();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        System.out.println("Request path: " + path);

        // /api/auth/** пропускаем без проверки
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

            // сохраняем email текущего пользователя в контекст запроса
            requestContext.setProperty("currentUserEmail", email);

        } catch (Exception e) {
            abort(requestContext);
        }
    }

    private void abort(ContainerRequestContext ctx) {
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }
}
