package com.ticketguard.domain.ticket.controller;


import com.ticketguard.domain.ticket.dto.TicketCreateRequest;
import com.ticketguard.domain.ticket.dto.TicketResponse;
import com.ticketguard.domain.ticket.service.TicketService;
import com.ticketguard.global.annotation.RequireActiveQueue;
import com.ticketguard.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @RequireActiveQueue
    public ResponseEntity<ApiResponse<TicketResponse>> issueTicket(@RequestBody TicketCreateRequest ticketCreateRequest) {
        TicketResponse ticketResponse = ticketService.issueTicket(ticketCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(ticketResponse));
    }

}
