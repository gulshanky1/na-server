package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ServiceRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceTransformer {

    public Service toEntity(ServiceRequest request) {

        return Service.builder()
                .name(request.getName())
                .shortDescription(request.getShortDescription())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .price(request.getPrice())
                .serviceType(request.getServiceType())
                .active(
                        request.getActive() != null
                                ? request.getActive()
                                : true
                )
                .build();
    }

    public void updateEntity(
            Service service,
            ServiceRequest request
    ) {

        service.setName(request.getName());
        service.setShortDescription(request.getShortDescription());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());

        if (request.getImageUrl() != null) {
            service.setImageUrl(request.getImageUrl());
        }

        if (request.getServiceType() != null) {
            service.setServiceType(request.getServiceType());
        }

        if (request.getActive() != null) {
            service.setActive(request.getActive());
        }
    }

    public ServiceResponse toResponse(Service service) {

        return ServiceResponse.builder()
                .serviceId(service.getServiceId())
                .name(service.getName())
                .shortDescription(service.getShortDescription())
                .description(service.getDescription())
                .imageUrl(service.getImageUrl())
                .price(service.getPrice())
                .serviceType(service.getServiceType())
                .active(service.isActive())
                .createdAt(service.getCreatedAt())
                .updatedAt(service.getUpdatedAt())
                .build();
    }
}