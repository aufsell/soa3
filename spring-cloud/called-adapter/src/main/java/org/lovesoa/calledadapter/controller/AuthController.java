package org.lovesoa.calledadapter.controller;

import org.lovesoa.calledadapter.dto.AuthResponse;
import org.lovesoa.calledadapter.dto.LoginRequest;
import org.lovesoa.calledadapter.dto.RegisterRequest;
import org.lovesoa.calledadapter.soap.AuthSoapClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthSoapClient authSoapClient;

    public AuthController(AuthSoapClient authSoapClient) {
        this.authSoapClient = authSoapClient;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authSoapClient.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authSoapClient.login(request);
        return ResponseEntity.ok(response);
    }
}
