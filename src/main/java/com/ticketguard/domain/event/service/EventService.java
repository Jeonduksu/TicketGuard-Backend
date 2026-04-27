package com.ticketguard.domain.event.service;

import com.ticketguard.domain.event.dto.EventResponse;
import com.ticketguard.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<EventResponse> getEvent(){
        return eventRepository.findAll().stream()
                .map(EventResponse::from)
                .toList();
    }
}
