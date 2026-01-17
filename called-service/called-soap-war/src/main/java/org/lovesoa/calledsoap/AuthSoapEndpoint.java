package org.lovesoa.calledsoap;

import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledejb.dtos.RegisterRequest;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@WebService(
    name = "AuthService",
    serviceName = "AuthService",
    targetNamespace = "http://soap.calledejb.lovesoa.org/",
    portName = "AuthServicePort"
)
public class AuthSoapEndpoint {

    private AuthServiceRemote getAuthService() {
        try {
            InitialContext ctx = new InitialContext();
            return (AuthServiceRemote) ctx.lookup("java:global/called-ejb/AuthServiceBean!org.lovesoa.calledejb.service.api.AuthServiceRemote");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup AuthService EJB", e);
        }
    }

    @WebMethod
    public AuthResponse register(@WebParam(name = "request") RegisterRequest request) {
        return getAuthService().register(request);
    }

    @WebMethod
    public AuthResponse login(@WebParam(name = "request") LoginRequest request) {
        return getAuthService().login(request);
    }
}
