package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.NotificationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.NotificationType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Notification;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.NotificationRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.NotificationTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final NotificationTransformer notificationTransformer;


    // =====================================================
    // CREATE NOTIFICATION
    // =====================================================

    @Transactional
    public NotificationResponse createNotification(
            Long userId,
            NotificationType type,
            String title,
            String message,
            Long referenceId,
            String referenceType
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with id: "
                                                + userId
                                )
                        );


        Notification notification =
                Notification.builder()

                        .user(user)

                        .type(type)

                        .title(title)

                        .message(message)

                        .referenceId(referenceId)

                        .referenceType(referenceType)

                        .read(false)

                        .build();


        Notification savedNotification =
                notificationRepository.save(
                        notification
                );


        return notificationTransformer.toResponse(
                savedNotification
        );
    }


    // =====================================================
    // GET MY NOTIFICATIONS
    // =====================================================

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(
            Long userId,
            Pageable pageable
    ) {

        return notificationRepository
                .findByUser_UserId(
                        userId,
                        pageable
                )
                .map(
                        notificationTransformer::toResponse
                );
    }


    // =====================================================
    // GET UNREAD NOTIFICATIONS
    // =====================================================

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUnreadNotifications(
            Long userId,
            Pageable pageable
    ) {

        return notificationRepository
                .findByUser_UserIdAndReadFalse(
                        userId,
                        pageable
                )
                .map(
                        notificationTransformer::toResponse
                );
    }


    // =====================================================
    // UNREAD COUNT
    // =====================================================

    @Transactional(readOnly = true)
    public long getUnreadCount(
            Long userId
    ) {

        return notificationRepository
                .countByUser_UserIdAndReadFalse(
                        userId
                );
    }


    // =====================================================
    // MARK AS READ
    // =====================================================

    @Transactional
    public void markAsRead(
            Long userId,
            Long notificationId
    ) {

        Notification notification =
                notificationRepository.findById(
                        notificationId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with id: "
                                        + notificationId
                        )
                );


        // Security check
        if (
                !notification
                        .getUser()
                        .getUserId()
                        .equals(userId)
        ) {

            throw new ResourceNotFoundException(
                    "Notification not found"
            );
        }


        notification.setRead(true);

        notificationRepository.save(
                notification
        );
    }


    // =====================================================
    // MARK ALL AS READ
    // =====================================================

    @Transactional
    public void markAllAsRead(
            Long userId
    ) {

        Page<Notification> notifications =
                notificationRepository
                        .findByUser_UserIdAndReadFalse(
                                userId,
                                Pageable.unpaged()
                        );


        notifications
                .getContent()
                .forEach(
                        notification ->
                                notification.setRead(true)
                );


        notificationRepository.saveAll(
                notifications.getContent()
        );
    }
}