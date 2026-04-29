package com.ticketguard.domain.queue.scheduler;

import com.ticketguard.domain.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueService queueService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WAITING_QUEUE_PATTERN = "queue:waiting:event:*";

    @Scheduled(fixedRate = 1000)
    public void processQueue() {
        Set<String> keys = redisTemplate.keys(WAITING_QUEUE_PATTERN);

        if(keys == null || keys.isEmpty()) {
            return;
        }

        for(String key : keys) {
            String eventId = key.substring(key.lastIndexOf(":")+1);

            long allowCount = 50;

            queueService.allowEntry(eventId, allowCount);
            log.info("[대기열 통과] 공연({}) - 맨 앞 {}명 입장 허가 완료!", eventId, allowCount);
        }
    }
}
