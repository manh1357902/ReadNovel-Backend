package com.example.readnovel.service;

import com.example.readnovel.payload.request.LoginRequest;
import com.example.readnovel.payload.request.RefreshTokenRequest;
import com.example.readnovel.payload.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> login(LoginRequest loginRequest);
    ResponseEntity<Object> register(RegisterRequest registerRequest);
    ResponseEntity<Object> refresh(RefreshTokenRequest tokenRequest);
}
