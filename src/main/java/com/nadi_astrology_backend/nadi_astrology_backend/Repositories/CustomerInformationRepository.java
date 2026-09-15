package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.CustomerInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerInformationRepository
        extends JpaRepository<CustomerInformation, Long> {

    Optional<CustomerInformation> findByOrder_OrderId(Long orderId);

    boolean existsByOrder_OrderId(Long orderId);
}