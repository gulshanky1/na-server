package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminCustomerInformationResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminOrderItemResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminOrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminOrderUserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CustomerInformation;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CustomerInformationRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.KundaliMilanDetailsRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminKundaliMilanResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.KundaliMilanDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final CustomerInformationRepository customerInformationRepository;
    private final KundaliMilanDetailsRepository kundaliMilanDetailsRepository;

    @Transactional(readOnly = true)
    public Page<AdminOrderResponse> getAllOrders(
            String search,
            com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus status,
            Pageable pageable
    ) {

        return orderRepository
                .searchAdminOrders(search, status, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public AdminOrderResponse getOrderById(Long orderId) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: " + orderId
                                )
                        );

        return toResponse(order);
    }

    private AdminOrderResponse toResponse(Order order) {

        AdminOrderUserResponse userResponse =
                null;

        if (order.getUser() != null) {

            userResponse =
                    AdminOrderUserResponse.builder()
                            .userId(order.getUser().getUserId())
                            .fullName(order.getUser().getFullName())
                            .email(order.getUser().getEmail())
                            .phone(order.getUser().getPhone())
                            .profileImage(order.getUser().getProfileImage())
                            .build();
        }

        CustomerInformation customerInformation =
                customerInformationRepository
                        .findByOrder_OrderId(order.getOrderId())
                        .orElse(null);

        AdminCustomerInformationResponse
                customerInformationResponse =
                customerInformation == null
                        ? null
                        : toCustomerInformationResponse(
                        customerInformation
                );

        List<AdminOrderItemResponse> items =
                order.getItems() == null
                        ? Collections.emptyList()
                        : order.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList();

        return AdminOrderResponse.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .user(userResponse)
                .customerInformation(
                        customerInformationResponse
                )
                .items(items)
                .build();
    }

    private AdminOrderItemResponse toItemResponse(
            OrderItem item
    ) {

        AdminKundaliMilanResponse kundaliMilanResponse = null;

        KundaliMilanDetails details =
                kundaliMilanDetailsRepository
                        .findByOrderItem_OrderItemId(
                                item.getOrderItemId()
                        )
                        .orElse(null);

        if (details != null) {

            kundaliMilanResponse =
                    AdminKundaliMilanResponse.builder()
                            .kundaliMilanId(
                                    details.getKundaliMilanId()
                            )
                            .boyName(
                                    details.getBoyName()
                            )
                            .boyDateOfBirth(
                                    details.getBoyDateOfBirth()
                            )
                            .boyBirthTime(
                                    details.getBoyBirthTime()
                            )
                            .boyBirthPlace(
                                    details.getBoyBirthPlace()
                            )
                            .girlName(
                                    details.getGirlName()
                            )
                            .girlDateOfBirth(
                                    details.getGirlDateOfBirth()
                            )
                            .girlBirthTime(
                                    details.getGirlBirthTime()
                            )
                            .girlBirthPlace(
                                    details.getGirlBirthPlace()
                            )
                            .createdAt(
                                    details.getCreatedAt()
                            )
                            .updatedAt(
                                    details.getUpdatedAt()
                            )
                            .build();
        }

        return AdminOrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productId(
                        item.getProduct() == null
                                ? null
                                : item.getProduct().getProductId()
                )
                .productName(item.getProductName())
                .productType(
                        item.getProduct() == null
                                ? null
                                : item.getProduct()
                                .getType()
                                .name()
                )
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .kundaliMilan(kundaliMilanResponse)
                .build();
    }
    private AdminCustomerInformationResponse
    toCustomerInformationResponse(
            CustomerInformation information
    ) {

        return AdminCustomerInformationResponse.builder()
                .customerInformationId(
                        information.getCustomerInformationId()
                )
                .fullName(information.getFullName())
                .email(information.getEmail())
                .phone(information.getPhone())
                .dateOfBirth(information.getDateOfBirth())
                .birthTime(information.getBirthTime())
                .birthPlace(information.getBirthPlace())
                .countryOfBirth(
                        information.getCountryOfBirth()
                )
                .address(information.getAddress())
                .city(information.getCity())
                .state(information.getState())
                .postalCode(
                        information.getPostalCode()
                )
                .currentLivingCountry(
                        information.getCurrentLivingCountry()
                )
                .question(information.getQuestion())
                .comments(information.getComments())
                .createdAt(information.getCreatedAt())
                .updatedAt(information.getUpdatedAt())
                .build();
    }
}