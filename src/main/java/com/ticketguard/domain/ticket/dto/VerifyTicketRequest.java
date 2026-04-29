package com.ticketguard.domain.ticket.dto;

import java.util.UUID;

public record VerifyTicketRequest(
        UUID ticketId,
        String totpCode,
        String scannerId
) {
}
