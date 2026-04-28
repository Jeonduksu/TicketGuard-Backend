package com.ticketguard.domain.ticket.service;

import com.ticketguard.domain.event.entity.Event;
import com.ticketguard.domain.event.repository.EventRepository;
import com.ticketguard.domain.ticket.dto.TicketCreateRequest;
import com.ticketguard.domain.ticket.dto.TicketResponse;
import com.ticketguard.domain.ticket.entity.Ticket;
import com.ticketguard.domain.ticket.repository.TicketRepository;
import com.ticketguard.domain.user.entity.User;
import com.ticketguard.domain.user.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public TicketResponse issueTicket(TicketCreateRequest ticketCreateRequest) {
        Event event = eventRepository.findById(ticketCreateRequest.eventId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공연입니다."));

        User user = userRepository.findById(ticketCreateRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();

        Ticket ticket = Ticket.builder()
                .event(event)
                .owner(user)
                .seat(ticketCreateRequest.seat())
                .totpSecretKey(secretKey)
                .status(Ticket.TicketStatus.ISSUED)
                .build();

        ticketRepository.save(ticket);

        return new TicketResponse(
                ticket.getId(),
                event.getTitle(),
                ticket.getSeat(),
                ticket.getTotpSecretKey()
        );
    }
}
