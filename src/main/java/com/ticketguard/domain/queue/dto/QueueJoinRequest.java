package com.ticketguard.domain.queue.dto;

import java.util.UUID;

public record QueueJoinRequest(
        UUID eventId,
        UUID userId
) {
}
