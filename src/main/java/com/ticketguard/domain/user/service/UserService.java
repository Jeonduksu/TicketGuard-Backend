package com.ticketguard.domain.user.service;

import com.ticketguard.domain.user.dto.UserSignupRequest;
import com.ticketguard.domain.user.entity.User;
import com.ticketguard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. 회원가입 로직 (유저 관리는 UserService의 역할)
    @Transactional
    public void signup(UserSignupRequest request) {
        // 이미 존재하는 이메일인지 확인
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화 및 저장
        User newUser = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .name(request.name())
                .build();

        userRepository.save(newUser);
    }
}
