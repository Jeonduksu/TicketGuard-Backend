package com.ticketguard.domain.event.repository;

import com.ticketguard.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    
    // 예매 가능한 공연 목록만 조회할 때 사용
    List<Event> findByStatus(Event.EventStatus status);
}
