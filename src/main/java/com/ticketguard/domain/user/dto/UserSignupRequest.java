package com.ticketguard.domain.user.dto;

public record UserSignupRequest(
        String email,
        String password,
        String name
) {
}
