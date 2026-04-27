package com.ticketguard.domain.auth.dto;

import com.ticketguard.domain.user.entity.User;

import java.util.UUID;

public record LoginResponse(
        String accessToken,
        int expiresIn,
        UserInfo user
) {
    public record UserInfo(
            UUID id,
            String name,
            String did
    ) {}
}
