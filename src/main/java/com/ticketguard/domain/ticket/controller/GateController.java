package com.ticketguard.domain.ticket.controller;

import com.ticketguard.domain.ticket.dto.VerifyTicketRequest;
import com.ticketguard.domain.ticket.dto.VerifyTicketResponse;
import com.ticketguard.domain.ticket.service.TicketVerifyService;
import com.ticketguard.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gate")
public class GateController {

    private final TicketVerifyService ticketVerifyService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<VerifyTicketResponse>> verifyTicket(@RequestBody VerifyTicketRequest verifyTicketRequest) {
        VerifyTicketResponse response = ticketVerifyService.verifyGate(verifyTicketRequest);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
