package com.ticketguard.domain.ticket.dto;

import java.util.UUID;

public record TicketResponse(
        UUID ticketId,
        String eventTitle,
        String seat,
        String totpSecretKey
) {
}
