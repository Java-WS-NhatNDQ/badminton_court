package com.re.badminton_court.repository;

import com.re.badminton_court.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    java.util.Optional<com.re.badminton_court.model.entity.RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}
