package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.ServiceFulfillment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceFulfillmentRepository
        extends JpaRepository<ServiceFulfillment, Long> {

    // ============================================================
    // FIND BY ORDER ITEM
    // ============================================================

    Optional<ServiceFulfillment> findByOrderItem_OrderItemId(
            Long orderItemId
    );


    // ============================================================
    // CHECK DUPLICATE FULFILLMENT
    // ============================================================

    boolean existsByOrderItem_OrderItemId(
            Long orderItemId
    );


    // ============================================================
    // ADMIN - FIND BY STATUS
    // ============================================================

    Page<ServiceFulfillment> findByStatus(
            ServiceFulfillmentStatus status,
            Pageable pageable
    );


    // ============================================================
    // ADMIN - FIND BY SERVICE TYPE
    // ============================================================

    Page<ServiceFulfillment> findByServiceType(
            ServiceType serviceType,
            Pageable pageable
    );


    // ============================================================
    // ADMIN - FIND BY STATUS + SERVICE TYPE
    // ============================================================

    Page<ServiceFulfillment> findByStatusAndServiceType(
            ServiceFulfillmentStatus status,
            ServiceType serviceType,
            Pageable pageable
    );


    // ============================================================
    // CUSTOMER - FIND THEIR SERVICE
    // ============================================================

    @org.springframework.data.jpa.repository.Query("""
        SELECT sf
        FROM ServiceFulfillment sf
        JOIN sf.orderItem oi
        JOIN oi.order o
        WHERE o.user.userId = :userId
        """)
    Page<ServiceFulfillment> findByUserId(
            Long userId,
            Pageable pageable
    );

    long countByStatus(ServiceFulfillmentStatus status);
    // ============================================================
    // CUSTOMER - FIND THEIR SERVICE BY ID
    // ============================================================

    @org.springframework.data.jpa.repository.Query("""
        SELECT sf
        FROM ServiceFulfillment sf
        JOIN sf.orderItem oi
        JOIN oi.order o
        WHERE sf.fulfillmentId = :fulfillmentId
        AND o.user.userId = :userId
        """)
    Optional<ServiceFulfillment> findByIdAndUserId(
            Long fulfillmentId,
            Long userId
    );
}