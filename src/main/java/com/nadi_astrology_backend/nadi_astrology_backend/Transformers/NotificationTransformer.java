package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.NotificationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationTransformer {

    public NotificationResponse toResponse(
            Notification notification
    ) {

        return NotificationResponse.builder()

                .notificationId(
                        notification.getNotificationId()
                )

                .type(
                        notification.getType()
                )

                .title(
                        notification.getTitle()
                )

                .message(
                        notification.getMessage()
                )

                .referenceId(
                        notification.getReferenceId()
                )

                .referenceType(
                        notification.getReferenceType()
                )

                .read(
                        notification.isRead()
                )

                .createdAt(
                        notification.getCreatedAt()
                )

                .build();
    }
}