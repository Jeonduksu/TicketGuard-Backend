package com.ticketguard.domain.event.dto;

import com.ticketguard.domain.event.entity.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID id,
        String title,
        LocalDateTime startAt,
        String venue,
        int totalSeas,
        String imageUrl,
        String category,
        int price
) {

    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getStartsAt(),
                event.getVenue(),
                event.getTotalSeats(),
                event.getImageUrl(),
                event.getCategory().name(),
                event.getPrice()
        );
    }
}
