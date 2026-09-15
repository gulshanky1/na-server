package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.PhoneConsultationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PhoneConsultationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.PhoneConsultation;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.PhoneConsultationRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.PhoneConsultationTransformer;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.DuplicateResourceException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhoneConsultationService {

    private final PhoneConsultationRepository phoneConsultationRepository;

    private final PhoneConsultationTransformer phoneConsultationTransformer;

    private final CloudinaryService cloudinaryService;

    private final ProductSyncService productSyncService;


    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    public PhoneConsultationResponse createConsultation(
            PhoneConsultationRequest request
    ) {

        String name = request.getName().trim();

        if (phoneConsultationRepository
                .existsByNameIgnoreCase(name)) {

            throw new DuplicateResourceException(
                    "Phone consultation already exists with name: "
                            + name
            );
        }

        PhoneConsultation consultation =
                phoneConsultationTransformer.toEntity(request);

        PhoneConsultation saved =
                phoneConsultationRepository.save(consultation);


        // =====================================================
        // AUTOMATICALLY CREATE PRODUCT
        // =====================================================

        productSyncService.createOrUpdateProduct(
                ProductType.PHONE_CONSULTATION,
                saved.getConsultationId(),
                saved.getName(),
                saved.getShortDescription(),
                saved.getImageUrl(),
                saved.getPrice(),
                saved.isActive()
        );


        return phoneConsultationTransformer.toResponse(saved);
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public PhoneConsultationResponse getConsultationById(
            Long consultationId
    ) {

        PhoneConsultation consultation =
                phoneConsultationRepository.findById(consultationId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Phone consultation not found with id: "
                                                + consultationId
                                )
                        );

        return phoneConsultationTransformer.toResponse(
                consultation
        );
    }


    // =========================================================
    // GET ACTIVE CONSULTATIONS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<PhoneConsultationResponse> getActiveConsultations(
            Pageable pageable
    ) {

        return phoneConsultationRepository
                .findByActiveTrue(pageable)
                .map(phoneConsultationTransformer::toResponse);
    }


    // =========================================================
    // GET ALL CONSULTATIONS - ADMIN
    // =========================================================

    @Transactional(readOnly = true)
    public Page<PhoneConsultationResponse> getAllConsultations(
            Pageable pageable
    ) {

        return phoneConsultationRepository
                .findAll(pageable)
                .map(phoneConsultationTransformer::toResponse);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Transactional
    public PhoneConsultationResponse updateConsultation(
            Long consultationId,
            PhoneConsultationRequest request
    ) {

        PhoneConsultation consultation =
                phoneConsultationRepository.findById(consultationId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Phone consultation not found with id: "
                                                + consultationId
                                )
                        );


        String newName = request.getName().trim();


        // =====================================================
        // CHECK DUPLICATE NAME
        // =====================================================

        phoneConsultationRepository
                .findByNameIgnoreCase(newName)
                .ifPresent(existing -> {

                    if (!existing.getConsultationId()
                            .equals(consultationId)) {

                        throw new DuplicateResourceException(
                                "Another phone consultation already exists with name: "
                                        + newName
                        );
                    }
                });


        // =====================================================
        // UPDATE CONSULTATION
        // =====================================================

        phoneConsultationTransformer.updateEntity(
                consultation,
                request
        );


        PhoneConsultation updated =
                phoneConsultationRepository.save(consultation);


        // =====================================================
        // SYNCHRONIZE PRODUCT
        // =====================================================

        productSyncService.createOrUpdateProduct(
                ProductType.PHONE_CONSULTATION,
                updated.getConsultationId(),
                updated.getName(),
                updated.getShortDescription(),
                updated.getImageUrl(),
                updated.getPrice(),
                updated.isActive()
        );


        return phoneConsultationTransformer.toResponse(
                updated
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Transactional
    public PhoneConsultationResponse deactivateConsultation(
            Long consultationId
    ) {

        PhoneConsultation consultation =
                phoneConsultationRepository.findById(consultationId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Phone consultation not found with id: "
                                                + consultationId
                                )
                        );

        consultation.setActive(false);

        PhoneConsultation saved =
                phoneConsultationRepository.save(consultation);


        // =====================================================
        // DEACTIVATE PRODUCT
        // =====================================================

        productSyncService.deactivateProduct(
                ProductType.PHONE_CONSULTATION,
                consultationId
        );


        return phoneConsultationTransformer.toResponse(saved);
    }


    // =========================================================
    // UPLOAD IMAGE
    // =========================================================

    @Transactional
    public PhoneConsultationResponse uploadConsultationImage(
            Long consultationId,
            MultipartFile file
    ) {

        PhoneConsultation consultation =
                phoneConsultationRepository.findById(consultationId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Phone consultation not found with id: "
                                                + consultationId
                                )
                        );


        // =====================================================
        // UPLOAD TO CLOUDINARY
        // =====================================================

        String imageUrl =
                cloudinaryService.uploadImage(file);


        // =====================================================
        // SAVE IMAGE URL IN CONSULTATION
        // =====================================================

        consultation.setImageUrl(imageUrl);

        PhoneConsultation saved =
                phoneConsultationRepository.save(consultation);


        // =====================================================
        // SYNCHRONIZE PRODUCT IMAGE
        // =====================================================

        productSyncService.createOrUpdateProduct(
                ProductType.PHONE_CONSULTATION,
                saved.getConsultationId(),
                saved.getName(),
                saved.getShortDescription(),
                saved.getImageUrl(),
                saved.getPrice(),
                saved.isActive()
        );


        return phoneConsultationTransformer.toResponse(
                saved
        );
    }
}