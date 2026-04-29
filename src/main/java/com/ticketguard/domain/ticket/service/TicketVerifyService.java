package com.ticketguard.domain.ticket.service;

import com.ticketguard.domain.ticket.dto.VerifyTicketRequest;
import com.ticketguard.domain.ticket.dto.VerifyTicketResponse;
import com.ticketguard.domain.ticket.entity.Ticket;
import com.ticketguard.domain.ticket.entity.TicketUseLog;
import com.ticketguard.domain.ticket.repository.TicketRepository;
import com.ticketguard.domain.ticket.repository.TicketUseLogRepository;
import com.ticketguard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketVerifyService {

    private final TicketRepository ticketRepository;
    private final TicketUseLogRepository ticketUseLogRepository;
    private final TotpService totpService;
    private final UserRepository userRepository;

    public VerifyTicketResponse verifyGate(VerifyTicketRequest request) {
        Ticket ticket = ticketRepository.findById(request.ticketId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 티켓입니다."));

        if(ticket.getStatus() != Ticket.TicketStatus.ISSUED) {
            saveLog(ticket, "FAIL_ALREADY_USER",request.scannerId());
            throw new IllegalArgumentException("이미 사용된 티켓이거나 만료된 상태입니다.");
        }

        boolean isValid = totpService.verifiCode(ticket.getTotpSecretKey(),request.totpCode());
        if(!isValid) {
            saveLog(ticket,"FAIL_INVALID_OTP",request.scannerId());
            throw new IllegalArgumentException("유효하지 않거나 만료된 QR 코드입니다.");
        }

        ticket.useTicket();

        ticketRepository.save(ticket);
        saveLog(ticket,"SUCCESS",request.scannerId());

        return new VerifyTicketResponse(true,ticket.getStatus().name(),ticket.getUsedAt());

    }

    private void saveLog(Ticket ticket, String result, String scannerId) {
        TicketUseLog ticketUseLog = TicketUseLog.builder()
                .ticket(ticket)
                .event(ticket.getEvent())
                .scannerId(scannerId)
                .result(result)
                .build();
        ticketUseLogRepository.save(ticketUseLog);
    }

}
