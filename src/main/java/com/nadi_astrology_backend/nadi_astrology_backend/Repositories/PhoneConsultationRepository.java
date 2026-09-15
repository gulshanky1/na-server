package com.nadi_astrology_backend.nadi_astrology_backend.Repositories;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ConsultationType;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.PhoneConsultation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneConsultationRepository
        extends JpaRepository<PhoneConsultation, Long> {

    Page<PhoneConsultation> findByActiveTrue(Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    Optional<PhoneConsultation> findByNameIgnoreCase(String name);

    Optional<PhoneConsultation> findByConsultationType(
            ConsultationType consultationType
    );
}