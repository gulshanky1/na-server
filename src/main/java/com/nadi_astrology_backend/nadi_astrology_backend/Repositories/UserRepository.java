package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    long countByAccountEnabledTrue();

    long countByEmailVerifiedTrue();

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderId(String providerId);
}