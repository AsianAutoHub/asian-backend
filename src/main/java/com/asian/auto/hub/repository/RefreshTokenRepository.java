package com.asian.auto.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asian.auto.hub.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByJti(String jti);
    void deleteByJti(String jti);
}
