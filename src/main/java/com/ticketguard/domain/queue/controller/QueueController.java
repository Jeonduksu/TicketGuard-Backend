package com.ticketguard.domain.queue.controller;

import com.ticketguard.domain.queue.dto.QueueJoinRequest;
import com.ticketguard.domain.queue.dto.QueueRankResponse;
import com.ticketguard.domain.queue.service.QueueService;
import com.ticketguard.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueService queueService;

    // 대기열 진입
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> joinQueue(@RequestBody QueueJoinRequest queueJoinRequest) {
        try {
            queueService.enqueue(queueJoinRequest.eventId().toString(), queueJoinRequest.userId().toString());
            return ResponseEntity.ok(ApiResponse.success(null));
        }catch (Exception e) {
            System.out.println("대기열 진입 중 에러 발생");
            e.printStackTrace();
            throw  e;
        }
    }

    // 대기열 순번 조회
    @GetMapping("/rank")
    public ResponseEntity<ApiResponse<QueueRankResponse>> getQueueRank(@RequestParam UUID eventId, @RequestParam UUID userId) {
        Long rank = queueService.getRank(eventId.toString(), userId.toString());

        String status = (rank == 0) ? "ACTIVE" : "WAITING";

        return ResponseEntity.ok(ApiResponse.success(new QueueRankResponse(rank, status)));

    }
}
