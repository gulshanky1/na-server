package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.KundaliMilanDetailsRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.KundaliMilanDetailsResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.KundaliMilanDetails;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.KundaliMilanDetailsRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderItemRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.KundaliMilanDetailsTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KundaliMilanDetailsService {

    private final KundaliMilanDetailsRepository kundaliMilanDetailsRepository;
    private final OrderItemRepository orderItemRepository;
    private final KundaliMilanDetailsTransformer kundaliMilanDetailsTransformer;

    @Transactional
    public KundaliMilanDetailsResponse createDetails(
            Long orderItemId,
            KundaliMilanDetailsRequest request
    ) {

        OrderItem orderItem =
                orderItemRepository.findById(orderItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order item not found with id: "
                                                + orderItemId
                                )
                        );

        if (kundaliMilanDetailsRepository
                .findByOrderItem_OrderItemId(orderItemId)
                .isPresent()) {

            throw new IllegalStateException(
                    "Kundali Milan details already exist for this order item"
            );
        }

        KundaliMilanDetails details =
                kundaliMilanDetailsTransformer
                        .toEntity(request);

        details.setOrderItem(orderItem);

        KundaliMilanDetails savedDetails =
                kundaliMilanDetailsRepository.save(details);

        return kundaliMilanDetailsTransformer
                .toResponse(savedDetails);
    }

    @Transactional(readOnly = true)
    public KundaliMilanDetailsResponse getDetailsByOrderItemId(
            Long orderItemId
    ) {

        KundaliMilanDetails details =
                kundaliMilanDetailsRepository
                        .findByOrderItem_OrderItemId(orderItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Kundali Milan details not found for order item id: "
                                                + orderItemId
                                )
                        );

        return kundaliMilanDetailsTransformer
                .toResponse(details);
    }

    @Transactional(readOnly = true)
    public KundaliMilanDetailsResponse getDetailsById(
            Long kundaliMilanId
    ) {

        KundaliMilanDetails details =
                kundaliMilanDetailsRepository
                        .findById(kundaliMilanId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Kundali Milan details not found with id: "
                                                + kundaliMilanId
                                )
                        );

        return kundaliMilanDetailsTransformer
                .toResponse(details);
    }

    @Transactional
    public KundaliMilanDetailsResponse updateDetails(
            Long kundaliMilanId,
            KundaliMilanDetailsRequest request
    ) {

        KundaliMilanDetails details =
                kundaliMilanDetailsRepository
                        .findById(kundaliMilanId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Kundali Milan details not found with id: "
                                                + kundaliMilanId
                                )
                        );

        kundaliMilanDetailsTransformer
                .updateEntity(details, request);

        KundaliMilanDetails updatedDetails =
                kundaliMilanDetailsRepository.save(details);

        return kundaliMilanDetailsTransformer
                .toResponse(updatedDetails);
    }
}