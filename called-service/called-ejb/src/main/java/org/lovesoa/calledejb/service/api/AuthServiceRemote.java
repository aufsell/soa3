package org.lovesoa.calledejb.service.api;

import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledejb.dtos.RegisterRequest;

import javax.ejb.Remote;

@Remote
public interface AuthServiceRemote {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
