package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.KundaliMilanDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KundaliMilanDetailsRepository
        extends JpaRepository<KundaliMilanDetails, Long> {

    Optional<KundaliMilanDetails>
    findByOrderItem_OrderItemId(Long orderItemId);

    boolean existsByOrderItem_OrderItemId(Long orderItemId);
}