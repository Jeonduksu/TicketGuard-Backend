package com.ticketguard.domain.user.repository;

import com.ticketguard.domain.user.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    // 1인 1기기 정책 검증용
    boolean existsByUserId(UUID userId);

    // 기기 지문으로 찾기
    Optional<Device> findByDeviceFingerprint(String deviceFingerprint);

    Optional<Device> findByUserId(UUID userId);
}
