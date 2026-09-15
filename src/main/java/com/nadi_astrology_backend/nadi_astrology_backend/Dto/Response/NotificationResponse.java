package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long notificationId;

    private NotificationType type;

    private String title;

    private String message;

    private Long referenceId;

    private String referenceType;

    private boolean read;

    private LocalDateTime createdAt;
}