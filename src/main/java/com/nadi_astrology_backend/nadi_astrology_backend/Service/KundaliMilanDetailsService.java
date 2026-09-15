package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.KundaliMilanDetailsRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.KundaliMilanDetails;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Service;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.KundaliMilanDetailsRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderItemRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ServiceRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.KundaliMilanDetailsTransformer;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class KundaliMilanDetailsService {

    private final KundaliMilanDetailsRepository detailsRepository;
    private final OrderItemRepository orderItemRepository;
    private final ServiceRepository serviceRepository;
    private final KundaliMilanDetailsTransformer transformer;


    // ============================================================
    // CREATE KUNDALI MILAN DETAILS
    // ============================================================

    @Transactional
    public void createDetails(
            Long orderItemId,
            KundaliMilanDetailsRequest request
    ) {

        if (request == null) {
            throw new BadRequestException(
                    "Kundali Milan details are required"
            );
        }


        // ========================================================
        // FIND ORDER ITEM
        // ========================================================

        OrderItem orderItem =
                orderItemRepository.findById(orderItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order item not found with id: "
                                                + orderItemId
                                )
                        );


        // ========================================================
        // VERIFY PRODUCT
        // ========================================================

        Product product = orderItem.getProduct();

        if (product == null) {
            throw new BadRequestException(
                    "Order item product is missing"
            );
        }

        if (product.getType() != ProductType.SERVICE) {
            throw new BadRequestException(
                    "Kundali Milan details can only be added to a service"
            );
        }


        // ========================================================
        // FIND ACTUAL SERVICE
        // ========================================================

        Long serviceId = product.getReferenceId();

        Service service =
                serviceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found with id: "
                                                + serviceId
                                )
                        );


        // ========================================================
        // VERIFY SERVICE TYPE
        // ========================================================

        if (service.getServiceType() != ServiceType.KUNDALI_MILAN) {
            throw new BadRequestException(
                    "Kundali Milan details are only allowed for Kundali Milan service"
            );
        }


        // ========================================================
        // PREVENT DUPLICATE DETAILS
        // ========================================================

        if (detailsRepository.existsByOrderItem_OrderItemId(orderItemId)) {
            throw new BadRequestException(
                    "Kundali Milan details already exist for this order item"
            );
        }


        // ========================================================
        // CREATE ENTITY
        // ========================================================

        KundaliMilanDetails details =
                transformer.toEntity(request);

        details.setOrderItem(orderItem);


        // ========================================================
        // SAVE
        // ========================================================

        detailsRepository.save(details);
    }
}