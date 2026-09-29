package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.EmailVerificationToken;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(
            String tokenHash
    );

    Optional<EmailVerificationToken> findByUser(
            User user
    );

    void deleteByUser(User user);
}