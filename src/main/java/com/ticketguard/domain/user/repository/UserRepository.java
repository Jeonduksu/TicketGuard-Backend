package com.ticketguard.domain.user.repository;

import com.ticketguard.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // 이메일로 유저 찾기
    Optional<User> findByEmail(String email);

    // DID로 유저 찾기(기기 인증시 필요)
    Optional<User> findByDid(String did);
}
