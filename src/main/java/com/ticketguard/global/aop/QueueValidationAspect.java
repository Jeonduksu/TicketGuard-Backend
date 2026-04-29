package com.ticketguard.global.aop;

import com.ticketguard.domain.ticket.dto.TicketCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class QueueValidationAspect {

    private final RedisTemplate<String,Object> redisTemplate;
    private static final String ACTIVE_QUEUE_PREFIX = "queue:active:event:";

    @Before("@annotation(com.ticketguard.global.annotation.RequireActiveQueue) && args(request, ..)")
    public void validateActiveQueue(TicketCreateRequest request) {
        String activeKey = ACTIVE_QUEUE_PREFIX + request.eventId();
        String userId = request.userId().toString();

        Boolean isActive = redisTemplate.opsForSet().isMember(activeKey, userId);

        if(isActive == null || !isActive) {
            log.warn("[부정 예매 시도 감지] eventId: {}, userId: {}", request.eventId(), userId);
            throw new IllegalArgumentException("대기열을 통과하지 않은 비정상적인 접근입니다.");
        }

        log.info("[예매 권한 확인됨] eventId: {}, userId: {}", request.eventId(), userId);
    }
}
