package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.CustomerServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.ServiceFulfillmentUpdateRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.ServiceFulfillmentResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Service;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.ServiceFulfillment;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderItemRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ServiceFulfillmentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ServiceRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.CustomerServiceFulfillmentTransformer;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.ServiceFulfillmentTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceFulfillmentService {

    private final ServiceFulfillmentRepository serviceFulfillmentRepository;
    private final OrderItemRepository orderItemRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceFulfillmentTransformer transformer;
    private final CustomerServiceFulfillmentTransformer customerTransformer;

    /**
     * Creates fulfillment after successful payment.
     *
     * This method is intentionally idempotent.
     * If fulfillment already exists for the order item,
     * it simply returns the existing record.
     */
    @Transactional
    public ServiceFulfillmentResponse createFulfillment(
            Long orderItemId
    ) {

        if (orderItemId == null) {
            throw new BadRequestException("Order item ID is required");
        }

        /*
         * Prevent duplicate fulfillment.
         */
        var existing =
                serviceFulfillmentRepository
                        .findByOrderItem_OrderItemId(orderItemId);

        if (existing.isPresent()) {
            return transformer.toResponse(existing.get());
        }

        /*
         * Find order item.
         */
        OrderItem orderItem =
                orderItemRepository.findById(orderItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order item not found with id: "
                                                + orderItemId
                                )
                        );

        /*
         * Product must exist.
         */
        Product product = orderItem.getProduct();

        if (product == null) {
            throw new BadRequestException(
                    "Product is missing for order item"
            );
        }

        /*
         * Only SERVICE products can create
         * ServiceFulfillment.
         */
        if (product.getType() != ProductType.SERVICE) {
            throw new BadRequestException(
                    "Service fulfillment can only be created "
                            + "for service products"
            );
        }

        /*
         * Product referenceId points to Service.serviceId.
         */
        Long serviceId = product.getReferenceId();

        if (serviceId == null) {
            throw new BadRequestException(
                    "Service reference ID is missing"
            );
        }

        /*
         * Load actual service.
         */
        Service service =
                serviceRepository.findById(serviceId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found with id: "
                                                + serviceId
                                )
                        );

        /*
         * Service type is required because it is
         * stored as a snapshot in fulfillment.
         */
        ServiceType serviceType =
                service.getServiceType();

        if (serviceType == null) {
            throw new BadRequestException(
                    "Service type is missing"
            );
        }

        /*
         * Create fulfillment.
         */
        ServiceFulfillment fulfillment =
                ServiceFulfillment.builder()
                        .orderItem(orderItem)
                        .serviceType(serviceType)
                        .status(ServiceFulfillmentStatus.PENDING)
                        .build();

        ServiceFulfillment saved =
                serviceFulfillmentRepository.save(fulfillment);

        return transformer.toResponse(saved);
    }

    /**
     * Get a service fulfillment by ID for a customer.
     *
     * Only the owner of the order can access it.
     */


    /**
     * Get all service fulfillments belonging
     * to the authenticated customer.
     */

    @Transactional(readOnly = true)
    public CustomerServiceFulfillmentResponse getMyCustomerFulfillment(
            Long fulfillmentId,
            Long userId
    ) {

        if (fulfillmentId == null || userId == null) {
            throw new BadRequestException(
                    "Fulfillment ID and user ID are required"
            );
        }

        ServiceFulfillment fulfillment =
                serviceFulfillmentRepository
                        .findByIdAndUserId(
                                fulfillmentId,
                                userId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service fulfillment not found"
                                )
                        );

        return customerTransformer.toResponse(
                fulfillment
        );
    }

    @Transactional(readOnly = true)
    public Page<CustomerServiceFulfillmentResponse>
    getMyCustomerFulfillments(
            Long userId,
            Pageable pageable
    ) {

        if (userId == null) {
            throw new BadRequestException(
                    "User ID is required"
            );
        }

        return serviceFulfillmentRepository
                .findByUserId(userId, pageable)
                .map(customerTransformer::toResponse);
    }

    /**
     * ADMIN:
     * Get all service fulfillments.
     */
    @Transactional(readOnly = true)
    public Page<ServiceFulfillmentResponse> getAllFulfillments(
            Pageable pageable
    ) {

        return serviceFulfillmentRepository
                .findAll(pageable)
                .map(transformer::toResponse);
    }

    @Transactional(readOnly = true)
    public ServiceFulfillmentResponse getFulfillmentById(
            Long fulfillmentId
    ) {

        if (fulfillmentId == null) {
            throw new BadRequestException(
                    "Fulfillment ID is required"
            );
        }

        ServiceFulfillment fulfillment =
                serviceFulfillmentRepository
                        .findById(fulfillmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service fulfillment not found with id: "
                                                + fulfillmentId
                                )
                        );

        return transformer.toResponse(fulfillment);
    }


    @Transactional(readOnly = true)
    public Page<ServiceFulfillmentResponse> getByStatus(
            ServiceFulfillmentStatus status,
            Pageable pageable
    ) {

        if (status == null) {
            throw new BadRequestException(
                    "Fulfillment status is required"
            );
        }

        return serviceFulfillmentRepository
                .findByStatus(status, pageable)
                .map(transformer::toResponse);
    }

    /**
     * ADMIN:
     * Update report, notes and status.
     */
    @Transactional
    public ServiceFulfillmentResponse updateFulfillment(
            Long fulfillmentId,
            ServiceFulfillmentUpdateRequest request
    ) {

        if (fulfillmentId == null) {
            throw new BadRequestException(
                    "Fulfillment ID is required"
            );
        }

        if (request == null) {
            throw new BadRequestException(
                    "Fulfillment update data is required"
            );
        }

        ServiceFulfillment fulfillment =
                serviceFulfillmentRepository
                        .findById(fulfillmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service fulfillment not found with id: "
                                                + fulfillmentId
                                )
                        );

        /*
         * Update report if supplied.
         */
        if (request.getReport() != null) {
            fulfillment.setReport(
                    request.getReport().trim()
            );
        }

        /*
         * Update internal admin notes.
         */
        if (request.getAdminNotes() != null) {
            fulfillment.setAdminNotes(
                    request.getAdminNotes().trim()
            );
        }

        /*
         * Update status.
         */
        if (request.getStatus() != null) {

            ServiceFulfillmentStatus newStatus =
                    request.getStatus();

            /*
             * COMPLETED requires a report.
             */
            if (newStatus ==
                    ServiceFulfillmentStatus.COMPLETED) {

                if (fulfillment.getReport() == null
                        || fulfillment.getReport().isBlank()) {

                    throw new BadRequestException(
                            "Report is required before "
                                    + "marking service as completed"
                    );
                }

                fulfillment.setCompletedAt(
                        java.time.LocalDateTime.now()
                );
            }

            /*
             * If service moves away from COMPLETED,
             * remove completed timestamp.
             */
            if (newStatus !=
                    ServiceFulfillmentStatus.COMPLETED) {

                fulfillment.setCompletedAt(null);
            }

            fulfillment.setStatus(newStatus);
        }

        ServiceFulfillment saved =
                serviceFulfillmentRepository.save(
                        fulfillment
                );

        return transformer.toResponse(saved);
    }

    /**
     * ADMIN:
     * Start working on a service.
     */
    @Transactional
    public ServiceFulfillmentResponse startFulfillment(
            Long fulfillmentId
    ) {

        ServiceFulfillment fulfillment =
                serviceFulfillmentRepository
                        .findById(fulfillmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service fulfillment not found with id: "
                                                + fulfillmentId
                                )
                        );

        if (fulfillment.getStatus() ==
                ServiceFulfillmentStatus.COMPLETED) {

            throw new BadRequestException(
                    "Completed service cannot be started again"
            );
        }

        if (fulfillment.getStatus() ==
                ServiceFulfillmentStatus.CANCELLED) {

            throw new BadRequestException(
                    "Cancelled service cannot be started"
            );
        }

        fulfillment.setStatus(
                ServiceFulfillmentStatus.IN_PROGRESS
        );

        ServiceFulfillment saved =
                serviceFulfillmentRepository.save(
                        fulfillment
                );

        return transformer.toResponse(saved);
    }

    /**
     * ADMIN:
     * Cancel a service fulfillment.
     */
    @Transactional
    public ServiceFulfillmentResponse cancelFulfillment(
            Long fulfillmentId
    ) {

        ServiceFulfillment fulfillment =
                serviceFulfillmentRepository
                        .findById(fulfillmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service fulfillment not found with id: "
                                                + fulfillmentId
                                )
                        );

        if (fulfillment.getStatus() ==
                ServiceFulfillmentStatus.COMPLETED) {

            throw new BadRequestException(
                    "Completed service cannot be cancelled"
            );
        }

        fulfillment.setStatus(
                ServiceFulfillmentStatus.CANCELLED
        );

        fulfillment.setCompletedAt(null);

        ServiceFulfillment saved =
                serviceFulfillmentRepository.save(
                        fulfillment
                );

        return transformer.toResponse(saved);
    }
}