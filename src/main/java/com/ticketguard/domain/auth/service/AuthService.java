package com.ticketguard.domain.auth.service;

import com.ticketguard.domain.auth.dto.LoginRequest;
import com.ticketguard.domain.auth.dto.LoginResponse;
import com.ticketguard.domain.user.entity.Device;
import com.ticketguard.domain.user.entity.User;
import com.ticketguard.domain.user.repository.DeviceRepository;
import com.ticketguard.domain.user.repository.UserRepository;
import com.ticketguard.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DeviceRepository deviceRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다"));

        if(!passwordEncoder.matches(loginRequest.password(),user.getPasswordHash())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        deviceRepository.findByUserId(user.getId()).ifPresentOrElse(
                device -> {
                  if(!device.getDeviceFingerprint().equals(loginRequest.deviceFingerprint())) {
                      throw new IllegalArgumentException("기기 변경이 필요합니다.");
                  }
                },
                () -> {
                    Device newDevice = Device.builder()
                            .user(user)
                            .deviceFingerprint(loginRequest.deviceFingerprint())
                            .build();
                    deviceRepository.save(newDevice);
                }
        );

        String accessToken = jwtTokenProvider.createToken(user.getEmail());

        return new LoginResponse(
                accessToken,
                3600,
                new LoginResponse.UserInfo(user.getId(),user.getName(),user.getDid())
        );
    }
}
