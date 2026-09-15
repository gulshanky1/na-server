package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ApiResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.NotificationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // =====================================================
    // GET ALL ADMIN NOTIFICATIONS
    // =====================================================

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            Pageable pageable
    ) {

        Page<NotificationResponse> notifications =
                notificationService.getMyNotifications(
                        authenticatedUser.getUserId(),
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.<Page<NotificationResponse>>builder()
                        .error(false)
                        .status(200)
                        .message("Admin notifications fetched successfully")
                        .payload(notifications)
                        .build()
        );
    }

    // =====================================================
    // GET UNREAD ADMIN NOTIFICATIONS
    // =====================================================

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            Pageable pageable
    ) {

        Page<NotificationResponse> notifications =
                notificationService.getUnreadNotifications(
                        authenticatedUser.getUserId(),
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.<Page<NotificationResponse>>builder()
                        .error(false)
                        .status(200)
                        .message("Unread admin notifications fetched successfully")
                        .payload(notifications)
                        .build()
        );
    }

    // =====================================================
    // GET UNREAD COUNT
    // =====================================================

    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        long count =
                notificationService.getUnreadCount(
                        authenticatedUser.getUserId()
                );

        return ResponseEntity.ok(
                ApiResponse.<Long>builder()
                        .error(false)
                        .status(200)
                        .message("Admin notification count fetched successfully")
                        .payload(count)
                        .build()
        );
    }

    // =====================================================
    // MARK AS READ
    // =====================================================

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long notificationId
    ) {

        notificationService.markAsRead(
                authenticatedUser.getUserId(),
                notificationId
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .error(false)
                        .status(200)
                        .message("Notification marked as read")
                        .payload(null)
                        .build()
        );
    }

    // =====================================================
    // MARK ALL AS READ
    // =====================================================

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        notificationService.markAllAsRead(
                authenticatedUser.getUserId()
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .error(false)
                        .status(200)
                        .message("All notifications marked as read")
                        .payload(null)
                        .build()
        );
    }
}