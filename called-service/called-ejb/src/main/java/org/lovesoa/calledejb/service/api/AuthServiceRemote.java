package org.lovesoa.calledejb.service.api;

import jakarta.ejb.Remote;
import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledejb.dtos.RegisterRequest;

@Remote
public interface AuthServiceRemote {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
