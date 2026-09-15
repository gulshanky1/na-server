package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Page<Notification> findByUser_UserId(
            Long userId,
            Pageable pageable
    );

    Page<Notification> findByUser_UserIdAndReadFalse(
            Long userId,
            Pageable pageable
    );

    long countByUser_UserIdAndReadFalse(
            Long userId
    );
}