package com.ticketguard.domain.user.controller;

import com.ticketguard.domain.user.dto.UserSignupRequest;
import com.ticketguard.domain.user.repository.UserRepository;
import com.ticketguard.domain.user.service.UserService;
import com.ticketguard.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody UserSignupRequest userSignupRequest) {
        userService.signup(userSignupRequest);

        return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> getMyInfo(Authentication authentication) {
        String userEmail = authentication.getName();
        String message = "현재 로그인된 사용자 이메일 : " + userEmail;

        return ResponseEntity.ok(ApiResponse.success(message));
    }

}
