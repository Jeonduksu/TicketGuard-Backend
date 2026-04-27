package com.ticketguard.domain.event.controller;

import com.ticketguard.domain.event.dto.EventResponse;
import com.ticketguard.domain.event.service.EventService;
import com.ticketguard.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEvents() {
        List<EventResponse> responseData = eventService.getEvent();
        return ResponseEntity.ok(ApiResponse.success(responseData));
    }
}
