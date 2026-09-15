package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.PhoneConsultationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PhoneConsultationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.PhoneConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/consultations")
@RequiredArgsConstructor
public class AdminPhoneConsultationController {

    private final PhoneConsultationService phoneConsultationService;


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public PhoneConsultationResponse createConsultation(
            @Valid @RequestBody PhoneConsultationRequest request
    ) {

        return phoneConsultationService.createConsultation(request);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public Page<PhoneConsultationResponse> getAllConsultations(
            Pageable pageable
    ) {

        return phoneConsultationService.getAllConsultations(pageable);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{consultationId}")
    public PhoneConsultationResponse getConsultationById(
            @PathVariable Long consultationId
    ) {

        return phoneConsultationService.getConsultationById(consultationId);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{consultationId}")
    public PhoneConsultationResponse updateConsultation(
            @PathVariable Long consultationId,
            @Valid @RequestBody PhoneConsultationRequest request
    ) {

        return phoneConsultationService.updateConsultation(
                consultationId,
                request
        );
    }


    // =========================================================
    // DELETE / DEACTIVATE
    // =========================================================

    @DeleteMapping("/{consultationId}")
    public PhoneConsultationResponse deactivateConsultation(
            @PathVariable Long consultationId
    ) {

        return phoneConsultationService.deactivateConsultation(
                consultationId
        );
    }


    // =========================================================
    // UPLOAD IMAGE
    // =========================================================

    @PostMapping(
            value = "/{consultationId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PhoneConsultationResponse uploadImage(
            @PathVariable Long consultationId,
            @RequestParam("file") MultipartFile file
    ) {

        return phoneConsultationService.uploadConsultationImage(
                consultationId,
                file
        );
    }
}