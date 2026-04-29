package com.ticketguard.domain.queue.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String WAITING_QUEUE_PREFIX = "queue:waiting:event:";
    private static final String ACTIVE_QUEUE_PREFIX = "queue:active:event:";

    // 대기열 진입
    public void enqueue(String eventId, String userId) {
        String waitingKey = WAITING_QUEUE_PREFIX + eventId;

        redisTemplate.opsForZSet().add(waitingKey, userId,System.currentTimeMillis());
    }
    
    // 대기 순번 확인
    public Long getRank(String eventId,String userId) {
        String activeKey = ACTIVE_QUEUE_PREFIX + eventId;
        String waitingKey = WAITING_QUEUE_PREFIX + eventId;
        
        Boolean isActive = redisTemplate.opsForSet().isMember(activeKey, userId);
        if(isActive != null && isActive) {
            return 0L;
        }
        
        Long rank = redisTemplate.opsForZSet().rank(waitingKey, userId);
        if(rank != null) {
            return rank+1;
        }
        
        return -1L;
    }
    
    // 입장 허가
    public void allowEntry(String eventId, long count) {
        String waitingKey = WAITING_QUEUE_PREFIX + eventId;
        String activeKey = ACTIVE_QUEUE_PREFIX + eventId;

        Set<Object> usersToAdmit = redisTemplate.opsForZSet().range(waitingKey, 0, count -1);

        if(usersToAdmit != null && !usersToAdmit.isEmpty()) {
            for(Object user : usersToAdmit) {
                redisTemplate.opsForSet().add(activeKey, user.toString());
            }

            redisTemplate.opsForZSet().remove(waitingKey, usersToAdmit.toArray());
        }

    }
}
