package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.PaymentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    List<Payment> findByOrder_OrderId(Long orderId);

    List<Payment> findByStatus(PaymentStatus status);

    boolean existsByRazorpayOrderId(String razorpayOrderId);

    boolean existsByRazorpayPaymentId(String razorpayPaymentId);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.status = :status
        """)
    BigDecimal getTotalRevenue(
            @Param("status") PaymentStatus status
    );

    



}