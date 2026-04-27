package com.ticketguard.domain.ticket.repository;

import com.ticketguard.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

    // 특정 유저가 예매한 내역 조회
    List<Ticket> findByOwnerId(UUID ownerId);

    // 특정 공연의 전체 예매 티켓 수 조회(잔여 좌석 계산용)
    long countByEventId(UUID eventId);

}
