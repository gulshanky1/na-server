package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(
            String orderNumber
    );

    Page<Order> findByUser_UserId(
            Long userId,
            Pageable pageable
    );

    Page<Order> findByStatus(
            OrderStatus status,
            Pageable pageable
    );

    boolean existsByOrderNumber(
            String orderNumber
    );

    long countByStatus(
            OrderStatus status
    );

    List<Order> findTop5ByStatusOrderByCreatedAtDesc(
            OrderStatus status
    );
}