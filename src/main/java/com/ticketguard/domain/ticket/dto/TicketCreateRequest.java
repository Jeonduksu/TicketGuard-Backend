package com.ticketguard.domain.ticket.dto;

import java.util.UUID;

public record TicketCreateRequest(
        UUID eventId,
        UUID userId,
        String seat
) {
}
