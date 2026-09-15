package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PhoneConsultationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.PhoneConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
public class PhoneConsultationController {

    private final PhoneConsultationService
            phoneConsultationService;


    // =========================================================
    // GET ACTIVE CONSULTATIONS
    // =========================================================

    @GetMapping
    public Page<PhoneConsultationResponse> getConsultations(
            Pageable pageable
    ) {

        return phoneConsultationService
                .getActiveConsultations(pageable);
    }


    // =========================================================
    // GET CONSULTATION BY ID
    // =========================================================

    @GetMapping("/{consultationId}")
    public PhoneConsultationResponse getConsultationById(
            @PathVariable Long consultationId
    ) {

        return phoneConsultationService
                .getConsultationById(consultationId);
    }
}