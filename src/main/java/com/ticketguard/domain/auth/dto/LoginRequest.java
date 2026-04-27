package com.ticketguard.domain.auth.dto;

public record LoginRequest(
        String email,
        String password,
        String deviceFingerprint
) {
}
