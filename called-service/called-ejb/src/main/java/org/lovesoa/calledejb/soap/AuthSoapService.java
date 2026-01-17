package org.lovesoa.calledejb.soap;

import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledejb.dtos.RegisterRequest;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(
    name = "AuthService",
    serviceName = "AuthService",
    targetNamespace = "http://soap.calledejb.lovesoa.org/"
)
@Stateless
public class AuthSoapService {

    @EJB
    private AuthServiceRemote authService;

    @WebMethod
    public AuthResponse register(@WebParam(name = "request") RegisterRequest request) {
        return authService.register(request);
    }

    @WebMethod
    public AuthResponse login(@WebParam(name = "request") LoginRequest request) {
        return authService.login(request);
    }
}
