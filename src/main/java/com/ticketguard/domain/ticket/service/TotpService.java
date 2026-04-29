package com.ticketguard.domain.ticket.service;

import com.ticketguard.domain.ticket.repository.TicketRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TotpService {

    public boolean verifiCode(String secret,String totpcode) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();

        try {
            int code= Integer.parseInt(totpcode);
            return gAuth.authorize(secret, code);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
