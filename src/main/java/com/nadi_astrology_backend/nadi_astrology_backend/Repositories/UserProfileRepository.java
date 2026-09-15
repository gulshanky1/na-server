package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser_UserId(Long userId);

    boolean existsByUser_UserId(Long userId);
}