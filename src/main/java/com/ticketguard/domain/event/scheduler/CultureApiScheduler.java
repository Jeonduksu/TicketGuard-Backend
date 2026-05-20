package com.ticketguard.domain.event.scheduler;

import com.ticketguard.domain.event.service.CultureApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CultureApiScheduler {

    private final CultureApiService cultureApiService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("[문화정보 API] 앱 시작 시 데이터 로드");
        cultureApiService.fetchAndSaveEvents();
    }

    // 매일 새벽 3시에 갱신
    @Scheduled(cron = "0 0 3 * * *")
    public void scheduledFetch() {
        log.info("[문화정보 API] 스케줄 데이터 갱신");
        cultureApiService.fetchAndSaveEvents();
    }
}
