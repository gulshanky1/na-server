package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

import java.util.List;

@Repository
public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_OrderId(
            Long orderId
    );
    @Query("""
        SELECT oi
        FROM OrderItem oi
        JOIN oi.product p
        WHERE oi.order.orderId = :orderId
        AND p.type = :productType
        AND p.referenceId = :referenceId
        """)
    Optional<OrderItem> findOrderItemForProduct(
            @Param("orderId") Long orderId,
            @Param("productType") ProductType productType,
            @Param("referenceId") Long referenceId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT oi
        FROM OrderItem oi
        WHERE oi.orderItemId = :orderItemId
        """)
    Optional<OrderItem> findByIdForUpdate(
            @Param("orderItemId") Long orderItemId
    );
}