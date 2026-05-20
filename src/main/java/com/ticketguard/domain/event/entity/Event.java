package com.ticketguard.domain.event.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime endsAt;

    private String venue;

    @Column(nullable = false)
    private Integer totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;

    @Column(nullable = false)
    private Integer price;

    public enum EventStatus {
        DRAFT,PUBLISHED,CLOSED
    }

    public enum EventCategory {
        CONCERT,MUSICAL,SPORTS,EXHIBITION,CLASSIC,ETC,THEATER,FAMILY,FESTIVAL
    }

}
