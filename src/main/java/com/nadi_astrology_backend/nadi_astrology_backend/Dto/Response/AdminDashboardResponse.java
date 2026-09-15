package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponse {

    private Overview overview;
    private SalesByType salesByType;
    private Fulfillment fulfillment;
    private Content content;
    private List<RecentOrder> recentOrders;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Overview {

        private long totalUsers;
        private long totalStudents;
        private long totalOrders;
        private BigDecimal totalRevenue;
        private long pendingServices;
        private long totalProducts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SalesByType {

        private long courses;
        private long books;
        private long services;
        private long phoneConsultations;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Fulfillment {

        private long pending;
        private long inProgress;
        private long completed;
        private long cancelled;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Content {

        private long publishedBlogs;
        private long draftBlogs;
        private long archivedBlogs;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecentOrder {

        private Long orderId;
        private String orderNumber;
        private String customerName;
        private String customerEmail;
        private BigDecimal totalAmount;
        private String status;
        private LocalDateTime createdAt;
    }
}