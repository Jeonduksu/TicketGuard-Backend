package com.ticketguard.global.config;

import com.ticketguard.domain.event.entity.Event;
import com.ticketguard.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventDataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;


    @Override
    public void run(String... args) throws Exception {
        if(eventRepository.count() > 0) {
            return;
        }

        LocalDateTime iuStart = LocalDateTime.now().plusDays(30);
        eventRepository.save(Event.builder()
                .title("2024 아이유(IU) HEREH WORLD TOUR CONCERT")
                .startsAt(iuStart)
                .endsAt(iuStart.plusHours(3))
                .venue("서울 월드컵 경기장")
                .totalSeats(50000)
                .status(Event.EventStatus.PUBLISHED)
                .build());

        LocalDateTime brunoStart = LocalDateTime.now().plusDays(14);
        eventRepository.save(Event.builder()
                .title("Bruno Mars Live in Seoul")
                .startsAt(brunoStart)
                .endsAt(brunoStart.plusHours(2))
                .venue("잠실 종합운동장")
                .totalSeats(45000)
                .status(Event.EventStatus.PUBLISHED)
                .build());

        LocalDateTime phantomStart = LocalDateTime.now().plusDays(60);
        eventRepository.save(Event.builder()
                .title("The Phantom of the Opera")
                .startsAt(phantomStart)
                .endsAt(phantomStart.plusDays(5))
                .venue("샤롯데씨어터")
                .totalSeats(1200)
                .status(Event.EventStatus.PUBLISHED)
                .build());

    }
}
