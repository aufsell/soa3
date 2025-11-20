package org.lovesoa.calledejb.service.impl;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledejb.dtos.RegisterRequest;
import org.lovesoa.calledejb.models.User;
import org.lovesoa.calledejb.security.jwt.JwtService;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class AuthServiceBean implements AuthServiceRemote {

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;

    private final JwtService jwtService = new JwtService();

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        em.persist(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            User user = em.createQuery(
                            "select u from User u where u.email = :email", User.class)
                    .setParameter("email", request.getEmail())
                    .getSingleResult();

            if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Invalid email or password");
            }

            String token = jwtService.generateToken(user);
            return new AuthResponse(token);

        } catch (NoResultException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}

