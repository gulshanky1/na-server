package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminDashboardResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.BlogStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.PaymentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.BlogRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.PaymentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ProductRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ServiceFulfillmentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    private final ServiceFulfillmentRepository serviceFulfillmentRepository;
    private final BlogRepository blogRepository;

    public AdminDashboardResponse getDashboard() {

        // ============================================================
        // REVENUE
        // ============================================================

        BigDecimal totalRevenue =
                paymentRepository.getTotalRevenue(
                        PaymentStatus.SUCCESS
                );

        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }


        // ============================================================
        // OVERVIEW
        // ============================================================

        AdminDashboardResponse.Overview overview =
                AdminDashboardResponse.Overview.builder()
                        .totalUsers(userRepository.count())
                        .totalStudents(studentRepository.count())
                        .totalOrders(orderRepository.count())
                        .totalRevenue(totalRevenue)
                        .pendingServices(
                                serviceFulfillmentRepository
                                        .countByStatus(
                                                ServiceFulfillmentStatus.PENDING
                                        )
                        )
                        .totalProducts(
                                productRepository.countByActiveTrue()
                        )
                        .build();


        // ============================================================
        // SALES BY TYPE
        // ============================================================

        AdminDashboardResponse.SalesByType salesByType =
                AdminDashboardResponse.SalesByType.builder()
                        .courses(
                                productRepository.countByType(
                                        ProductType.COURSE
                                )
                        )
                        .books(
                                productRepository.countByType(
                                        ProductType.BOOK
                                )
                        )
                        .services(
                                productRepository.countByType(
                                        ProductType.SERVICE
                                )
                        )
                        .phoneConsultations(
                                productRepository.countByType(
                                        ProductType.PHONE_CONSULTATION
                                )
                        )
                        .build();


        // ============================================================
        // FULFILLMENT
        // ============================================================

        AdminDashboardResponse.Fulfillment fulfillment =
                AdminDashboardResponse.Fulfillment.builder()
                        .pending(
                                serviceFulfillmentRepository.countByStatus(
                                        ServiceFulfillmentStatus.PENDING
                                )
                        )
                        .inProgress(
                                serviceFulfillmentRepository.countByStatus(
                                        ServiceFulfillmentStatus.IN_PROGRESS
                                )
                        )
                        .completed(
                                serviceFulfillmentRepository.countByStatus(
                                        ServiceFulfillmentStatus.COMPLETED
                                )
                        )
                        .cancelled(
                                serviceFulfillmentRepository.countByStatus(
                                        ServiceFulfillmentStatus.CANCELLED
                                )
                        )
                        .build();


        // ============================================================
        // CONTENT
        // ============================================================

        AdminDashboardResponse.Content content =
                AdminDashboardResponse.Content.builder()
                        .publishedBlogs(
                                blogRepository.countByStatus(
                                        BlogStatus.PUBLISHED
                                )
                        )
                        .draftBlogs(
                                blogRepository.countByStatus(
                                        BlogStatus.DRAFT
                                )
                        )
                        .archivedBlogs(
                                blogRepository.countByStatus(
                                        BlogStatus.ARCHIVED
                                )
                        )
                        .build();


        // ============================================================
        // RECENT ORDERS
        // ============================================================

        List<Order> orders =
                orderRepository.findTop5ByStatusOrderByCreatedAtDesc(
                        OrderStatus.PAID
                );

        List<AdminDashboardResponse.RecentOrder> recentOrders =
                orders.stream()
                        .map(order ->
                                AdminDashboardResponse.RecentOrder.builder()
                                        .orderId(order.getOrderId())
                                        .orderNumber(order.getOrderNumber())
                                        .customerName(
                                                order.getUser().getFullName()
                                        )
                                        .customerEmail(
                                                order.getUser().getEmail()
                                        )
                                        .totalAmount(order.getTotalAmount())
                                        .status(
                                                order.getStatus().name()
                                        )
                                        .createdAt(order.getCreatedAt())
                                        .build()
                        )
                        .toList();


        // ============================================================
        // FINAL RESPONSE
        // ============================================================

        return AdminDashboardResponse.builder()
                .overview(overview)
                .salesByType(salesByType)
                .fulfillment(fulfillment)
                .content(content)
                .recentOrders(recentOrders)
                .build();
    }
}