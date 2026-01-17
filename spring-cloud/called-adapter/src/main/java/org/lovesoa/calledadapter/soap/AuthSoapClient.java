package org.lovesoa.calledadapter.soap;

import jakarta.xml.ws.Service;
import org.lovesoa.calledadapter.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.net.URL;

@Component
public class AuthSoapClient {

    @Value("${soap.service.url}")
    private String soapServiceUrl;

    private static final String NAMESPACE_URI = "http://soap.calledejb.lovesoa.org/";
    private static final String SERVICE_NAME = "AuthService";

    public AuthResponse register(RegisterRequest request) {
        try {
            AuthSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.RegisterRequest soapRequest = 
                toSoapRegisterRequest(request);
            org.lovesoa.calledadapter.soap.model.AuthResponse soapResponse = 
                port.register(soapRequest);
            return fromSoapAuthResponse(soapResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            AuthSoapServiceInterface port = getPort();
            org.lovesoa.calledadapter.soap.model.LoginRequest soapRequest = 
                toSoapLoginRequest(request);
            org.lovesoa.calledadapter.soap.model.AuthResponse soapResponse = 
                port.login(soapRequest);
            return fromSoapAuthResponse(soapResponse);
        } catch (Exception e) {
            throw new RuntimeException("Error calling SOAP service: " + e.getMessage(), e);
        }
    }

    private AuthSoapServiceInterface getPort() throws Exception {
        URL wsdlLocation = new URL(soapServiceUrl + "/AuthService/AuthService?wsdl");
        QName serviceName = new QName(NAMESPACE_URI, SERVICE_NAME);
        Service service = Service.create(wsdlLocation, serviceName);
        return service.getPort(AuthSoapServiceInterface.class);
    }

    private org.lovesoa.calledadapter.soap.model.RegisterRequest toSoapRegisterRequest(RegisterRequest dto) {
        org.lovesoa.calledadapter.soap.model.RegisterRequest soap = 
            new org.lovesoa.calledadapter.soap.model.RegisterRequest();
        soap.setEmail(dto.getEmail());
        soap.setPassword(dto.getPassword());
        soap.setName(dto.getName());
        return soap;
    }

    private org.lovesoa.calledadapter.soap.model.LoginRequest toSoapLoginRequest(LoginRequest dto) {
        org.lovesoa.calledadapter.soap.model.LoginRequest soap = 
            new org.lovesoa.calledadapter.soap.model.LoginRequest();
        soap.setEmail(dto.getEmail());
        soap.setPassword(dto.getPassword());
        return soap;
    }

    private AuthResponse fromSoapAuthResponse(org.lovesoa.calledadapter.soap.model.AuthResponse soap) {
        AuthResponse dto = new AuthResponse();
        dto.setToken(soap.getToken());
        return dto;
    }
}
