package com.ticketguard.domain.auth.controller;

import com.ticketguard.domain.auth.dto.LoginRequest;
import com.ticketguard.domain.auth.dto.LoginResponse;
import com.ticketguard.domain.auth.service.AuthService;
import com.ticketguard.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);

        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }
}
