package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.RefreshToken;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(
            String tokenHash
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT rt
            FROM RefreshToken rt
            WHERE rt.tokenHash = :tokenHash
            """)
    Optional<RefreshToken> findByTokenHashForUpdate(
            @Param("tokenHash") String tokenHash
    );

    List<RefreshToken> findByTokenFamily(
            String tokenFamily
    );

    List<RefreshToken> findByUser(
            User user
    );
}