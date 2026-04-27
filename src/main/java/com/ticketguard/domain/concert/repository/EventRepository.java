package com.ticketguard.domain.concert.repository;

import com.ticketguard.domain.concert.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    
    // 예매 가능한 공연 목록만 조회할 때 사용
    List<Event> findByStatus(Event.EventStatus status);
}
