package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository
        extends JpaRepository<Service, Long> {

    Page<Service> findByActiveTrue(Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    Optional<Service> findByNameIgnoreCase(String name);
}