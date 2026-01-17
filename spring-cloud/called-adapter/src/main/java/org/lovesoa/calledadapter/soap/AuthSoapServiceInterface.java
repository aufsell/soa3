package org.lovesoa.calledadapter.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import org.lovesoa.calledadapter.soap.model.*;

@WebService(
    name = "AuthService",
    targetNamespace = "http://soap.calledejb.lovesoa.org/"
)
public interface AuthSoapServiceInterface {

    @WebMethod
    AuthResponse register(@WebParam(name = "request") RegisterRequest request);

    @WebMethod
    AuthResponse login(@WebParam(name = "request") LoginRequest request);
}
