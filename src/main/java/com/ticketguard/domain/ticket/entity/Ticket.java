package com.ticketguard.domain.ticket.entity;

import com.ticketguard.domain.event.entity.Event;
import com.ticketguard.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id",nullable = false)
    private User owner;

    private String seat;

    @Column(nullable = false)
    private String totpSecretKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    public enum TicketStatus {
        ISSUED,USED,EXPIRED,REVOKED
    }

    public void useTicket() {
        this.status = TicketStatus.USED;
        this.usedAt = LocalDateTime.now();
    }
}
