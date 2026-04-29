package com.ticketguard.domain.ticket.repository;

import com.ticketguard.domain.ticket.entity.TicketUseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketUseLogRepository extends JpaRepository<TicketUseLog, UUID> {
}
