package com.ticketguard.domain.ticket.dto;

import java.time.LocalDateTime;

public record VerifyTicketResponse(
        boolean valid,
        String status,
        LocalDateTime userAt
) {
}
