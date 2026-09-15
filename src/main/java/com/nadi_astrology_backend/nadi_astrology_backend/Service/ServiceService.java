package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ServiceRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.DuplicateResourceException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Service;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ServiceRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.ServiceTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    private final ServiceTransformer serviceTransformer;

    private final CloudinaryService cloudinaryService;

    private final ProductSyncService productSyncService;


    // ============================================================
    // CREATE SERVICE
    // ============================================================

    // ============================================================
// CREATE SERVICE + IMAGE
// ============================================================

    @Transactional
    public ServiceResponse createService(
            ServiceRequest request,
            MultipartFile file
    ) {

        String name = request.getName().trim();

        // --------------------------------------------------------
        // Check duplicate service
        // --------------------------------------------------------

        if (serviceRepository.existsByNameIgnoreCase(name)) {

            throw new DuplicateResourceException(
                    "Service already exists: " + name
            );
        }


        // --------------------------------------------------------
// Convert request → entity
// --------------------------------------------------------

        Service service =
                serviceTransformer.toEntity(request);

        if (service.getServiceType() == null) {
            service.setServiceType(
                    com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType.STANDARD
            );
        }


        // --------------------------------------------------------
        // Save service
        // --------------------------------------------------------

        Service savedService =
                serviceRepository.save(service);


        // --------------------------------------------------------
        // Automatically create Product
        // --------------------------------------------------------

        productSyncService.createOrUpdateProduct(
                ProductType.SERVICE,
                savedService.getServiceId(),
                savedService.getName(),
                savedService.getShortDescription(),
                savedService.getImageUrl(),
                savedService.getPrice(),
                savedService.isActive()
        );


        return serviceTransformer.toResponse(savedService);
    }


    // ============================================================
    // GET SERVICE BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long serviceId) {

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found with id: " + serviceId
                        )
                );

        return serviceTransformer.toResponse(service);
    }


    // ============================================================
    // GET ACTIVE SERVICES
    // ============================================================

    @Transactional(readOnly = true)
    public Page<ServiceResponse> getActiveServices(
            Pageable pageable
    ) {

        return serviceRepository
                .findByActiveTrue(pageable)
                .map(serviceTransformer::toResponse);
    }


    // ============================================================
    // GET ALL SERVICES
    // ============================================================

    @Transactional(readOnly = true)
    public Page<ServiceResponse> getAllServices(
            Pageable pageable
    ) {

        return serviceRepository
                .findAll(pageable)
                .map(serviceTransformer::toResponse);
    }


    // ============================================================
    // UPDATE SERVICE
    // ============================================================

    @Transactional
    public ServiceResponse updateService(
            Long serviceId,
            ServiceRequest request
    ) {

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found with id: " + serviceId
                        )
                );


        String name = request.getName().trim();


        // --------------------------------------------------------
        // Check duplicate service name
        // --------------------------------------------------------

        serviceRepository
                .findByNameIgnoreCase(name)
                .ifPresent(existingService -> {

                    if (!existingService.getServiceId()
                            .equals(serviceId)) {

                        throw new DuplicateResourceException(
                                "Service already exists: " + name
                        );
                    }
                });


        // --------------------------------------------------------
        // Update entity
        // --------------------------------------------------------

        serviceTransformer.updateEntity(
                service,
                request
        );


        Service updatedService =
                serviceRepository.save(service);


        // --------------------------------------------------------
        // Synchronize Product
        // --------------------------------------------------------

        productSyncService.createOrUpdateProduct(
                ProductType.SERVICE,
                updatedService.getServiceId(),
                updatedService.getName(),
                updatedService.getShortDescription(),
                updatedService.getImageUrl(),
                updatedService.getPrice(),
                updatedService.isActive()
        );

        return serviceTransformer.toResponse(updatedService);
    }


    // ============================================================
    // DEACTIVATE SERVICE
    // ============================================================

    @Transactional
    public ServiceResponse deactivateService(Long serviceId) {

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found with id: " + serviceId
                        )
                );

        service.setActive(false);

        Service savedService =
                serviceRepository.save(service);


        // --------------------------------------------------------
        // Deactivate Product
        // --------------------------------------------------------

        productSyncService.deactivateProduct(
                ProductType.SERVICE,
                serviceId
        );

        return serviceTransformer.toResponse(savedService);
    }


    // ============================================================
    // UPLOAD SERVICE IMAGE
    // ============================================================

    @Transactional
    public ServiceResponse uploadServiceImage(
            Long serviceId,
            MultipartFile file
    ) {

        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Service not found with id: " + serviceId
                        )
                );


        // --------------------------------------------------------
        // Upload image to Cloudinary
        // --------------------------------------------------------

        String imageUrl =
                cloudinaryService.uploadImage(file);


        // --------------------------------------------------------
        // Save image URL
        // --------------------------------------------------------

        service.setImageUrl(imageUrl);

        Service savedService =
                serviceRepository.save(service);


        // --------------------------------------------------------
        // Synchronize Product image
        // --------------------------------------------------------

        productSyncService.createOrUpdateProduct(
                ProductType.SERVICE,
                savedService.getServiceId(),
                savedService.getName(),
                savedService.getShortDescription(),
                savedService.getImageUrl(),
                savedService.getPrice(),
                savedService.isActive()
        );

        return serviceTransformer.toResponse(savedService);
    }
}