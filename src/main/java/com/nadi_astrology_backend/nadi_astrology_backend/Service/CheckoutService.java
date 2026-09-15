package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CheckoutItemRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CheckoutRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CheckoutServiceDetailsRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.CustomerInformationRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.KundaliMilanDetailsRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.OrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.CustomerInformation;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.KundaliMilanDetails;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Service;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.CustomerInformationRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.KundaliMilanDetailsRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ProductRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.ServiceRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.KundaliMilanDetailsTransformer;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.OrderTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CheckoutService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerInformationRepository customerInformationRepository;
    private final ServiceRepository serviceRepository;
    private final KundaliMilanDetailsRepository kundaliMilanDetailsRepository;
    private final KundaliMilanDetailsTransformer kundaliMilanDetailsTransformer;
    private final OrderTransformer orderTransformer;


    // ============================================================
    // CREATE CHECKOUT / ORDER
    // ============================================================

    @Transactional
    public OrderResponse createCheckout(CheckoutRequest request) {

        // --------------------------------------------------------
        // 1. VALIDATE REQUEST
        // --------------------------------------------------------

        if (request == null) {
            throw new BadRequestException(
                    "Checkout request is required"
            );
        }

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new BadRequestException(
                    "At least one product is required"
            );
        }

        if (request.getCustomerInformation() == null) {
            throw new BadRequestException(
                    "Customer information is required"
            );
        }


        // --------------------------------------------------------
        // 2. GET LOGGED-IN USER FROM JWT
        // --------------------------------------------------------

        Long userId = getAuthenticatedUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );


        // --------------------------------------------------------
        // 3. PREVENT DUPLICATE PRODUCT IDs
        // --------------------------------------------------------

        Set<Long> productIds = new HashSet<>();

        for (CheckoutItemRequest itemRequest :
                request.getItems()) {

            if (itemRequest.getProductId() == null) {
                throw new BadRequestException(
                        "Product ID is required"
                );
            }

            if (itemRequest.getQuantity() == null ||
                    itemRequest.getQuantity() < 1) {

                throw new BadRequestException(
                        "Quantity must be at least 1"
                );
            }

            if (!productIds.add(
                    itemRequest.getProductId())) {

                throw new BadRequestException(
                        "Duplicate product in checkout: "
                                + itemRequest.getProductId()
                );
            }
        }


        // --------------------------------------------------------
        // 4. VALIDATE SERVICE-SPECIFIC DETAILS
        // --------------------------------------------------------

        validateServiceDetails(
                request,
                productIds
        );


        // --------------------------------------------------------
        // 5. CREATE ORDER
        // --------------------------------------------------------

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .totalAmount(BigDecimal.ZERO)
                .status(OrderStatus.PAYMENT_PENDING)
                .build();


        // --------------------------------------------------------
        // 6. CREATE ORDER ITEMS
        // --------------------------------------------------------

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CheckoutItemRequest itemRequest :
                request.getItems()) {

            Product product = productRepository
                    .findByProductIdAndActiveTrue(
                            itemRequest.getProductId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Active product not found with id: "
                                            + itemRequest.getProductId()
                            )
                    );

            // ----------------------------------------------------
            // IMPORTANT:
            // Price comes from database.
            // Frontend cannot control the price.
            // ----------------------------------------------------

            BigDecimal unitPrice = product.getPrice();

            BigDecimal itemTotal = unitPrice.multiply(
                    BigDecimal.valueOf(
                            itemRequest.getQuantity()
                    )
            );


            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .unitPrice(unitPrice)
                    .quantity(itemRequest.getQuantity())
                    .totalPrice(itemTotal)
                    .build();

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(itemTotal);
        }


        // --------------------------------------------------------
        // 7. SET FINAL ORDER TOTAL
        // --------------------------------------------------------

        order.setTotalAmount(totalAmount);


        // --------------------------------------------------------
        // 8. SAVE ORDER
        // --------------------------------------------------------

        Order savedOrder = orderRepository.save(order);


        // --------------------------------------------------------
        // 9. SAVE CUSTOMER INFORMATION
        // --------------------------------------------------------

        CustomerInformationRequest customerRequest =
                request.getCustomerInformation();

        CustomerInformation customerInformation =
                CustomerInformation.builder()
                        .order(savedOrder)

                        // Basic information
                        .fullName(customerRequest.getFullName())
                        .email(customerRequest.getEmail())
                        .phone(customerRequest.getPhone())

                        // Astrology information
                        .dateOfBirth(
                                customerRequest.getDateOfBirth()
                        )
                        .birthTime(
                                customerRequest.getBirthTime()
                        )
                        .birthPlace(
                                customerRequest.getBirthPlace()
                        )
                        .countryOfBirth(
                                customerRequest.getCountryOfBirth()
                        )

                        // Address
                        .address(
                                customerRequest.getAddress()
                        )
                        .city(
                                customerRequest.getCity()
                        )
                        .state(
                                customerRequest.getState()
                        )
                        .postalCode(
                                customerRequest.getPostalCode()
                        )
                        .currentLivingCountry(
                                customerRequest
                                        .getCurrentLivingCountry()
                        )

                        // Questions
                        .question(
                                customerRequest.getQuestion()
                        )
                        .comments(
                                customerRequest.getComments()
                        )

                        .build();

        customerInformationRepository.save(
                customerInformation
        );


        // --------------------------------------------------------
        // 10. SAVE KUNDALI MILAN DETAILS
        // --------------------------------------------------------

        saveKundaliMilanDetails(
                savedOrder,
                request
        );


        // --------------------------------------------------------
        // 11. RETURN RESPONSE
        // --------------------------------------------------------

        return orderTransformer.toResponse(savedOrder);
    }


    // ============================================================
    // VALIDATE SERVICE DETAILS
    // ============================================================

    private void validateServiceDetails(
            CheckoutRequest request,
            Set<Long> productIds
    ) {

        List<CheckoutServiceDetailsRequest> serviceDetails =
                request.getServiceDetails();


        // --------------------------------------------------------
        // NO SERVICE DETAILS PROVIDED
        // --------------------------------------------------------

        if (serviceDetails == null ||
                serviceDetails.isEmpty()) {

            /*
             * Normal products do not require service details.
             *
             * But if the checkout contains a Kundali Milan
             * service, details are mandatory.
             */

            for (Long productId : productIds) {

                Product product = productRepository
                        .findByProductIdAndActiveTrue(productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active product not found with id: "
                                                + productId
                                )
                        );


                if (product.getType() ==
                        ProductType.SERVICE) {

                    Service service = serviceRepository
                            .findById(
                                    product.getReferenceId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Service not found with id: "
                                                    + product.getReferenceId()
                                    )
                            );


                    if (service.getServiceType() ==
                            ServiceType.KUNDALI_MILAN) {

                        throw new BadRequestException(
                                "Kundali Milan details are required"
                        );
                    }
                }
            }

            return;
        }


        // --------------------------------------------------------
        // PREVENT DUPLICATE SERVICE DETAILS
        // --------------------------------------------------------

        Set<Long> serviceDetailProductIds =
                new HashSet<>();


        for (CheckoutServiceDetailsRequest detailRequest :
                serviceDetails) {

            if (detailRequest.getProductId() == null) {

                throw new BadRequestException(
                        "Service details product ID is required"
                );
            }


            // ----------------------------------------------------
            // Product must actually be in checkout
            // ----------------------------------------------------

            if (!productIds.contains(
                    detailRequest.getProductId())) {

                throw new BadRequestException(
                        "Service details product is not included "
                                + "in checkout: "
                                + detailRequest.getProductId()
                );
            }


            // ----------------------------------------------------
            // Prevent duplicate service detail records
            // ----------------------------------------------------

            if (!serviceDetailProductIds.add(
                    detailRequest.getProductId())) {

                throw new BadRequestException(
                        "Duplicate service details for product: "
                                + detailRequest.getProductId()
                );
            }


            // ----------------------------------------------------
            // Resolve actual Product from database
            // ----------------------------------------------------

            Product product = productRepository
                    .findByProductIdAndActiveTrue(
                            detailRequest.getProductId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Active product not found with id: "
                                            + detailRequest.getProductId()
                            )
                    );


            // ----------------------------------------------------
            // Service details only for SERVICE products
            // ----------------------------------------------------

            if (product.getType() !=
                    ProductType.SERVICE) {

                throw new BadRequestException(
                        "Service details can only be added "
                                + "to service products"
                );
            }


            // ----------------------------------------------------
            // Resolve actual Service
            // ----------------------------------------------------

            Service service = serviceRepository
                    .findById(
                            product.getReferenceId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Service not found with id: "
                                            + product.getReferenceId()
                            )
                    );


            // ----------------------------------------------------
            // BACKEND decides the service type
            // ----------------------------------------------------

            if (service.getServiceType() ==
                    ServiceType.KUNDALI_MILAN) {

                // Kundali Milan requires Kundali information

                if (detailRequest.getKundaliMilan() == null) {

                    throw new BadRequestException(
                            "Kundali Milan details are required"
                    );
                }

            } else {

                // Standard service cannot receive
                // Kundali Milan information

                if (detailRequest.getKundaliMilan() != null) {

                    throw new BadRequestException(
                            "Kundali Milan details are only allowed "
                                    + "for Kundali Milan service"
                    );
                }
            }
        }


        // --------------------------------------------------------
        // EVERY KUNDALI SERVICE MUST HAVE DETAILS
        // --------------------------------------------------------

        for (Long productId : productIds) {

            Product product = productRepository
                    .findByProductIdAndActiveTrue(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Active product not found with id: "
                                            + productId
                            )
                    );


            if (product.getType() !=
                    ProductType.SERVICE) {
                continue;
            }


            Service service = serviceRepository
                    .findById(
                            product.getReferenceId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Service not found with id: "
                                            + product.getReferenceId()
                            )
                    );


            if (service.getServiceType() ==
                    ServiceType.KUNDALI_MILAN &&
                    !serviceDetailProductIds.contains(
                            productId
                    )) {

                throw new BadRequestException(
                        "Kundali Milan details are required "
                                + "for product: "
                                + productId
                );
            }
        }
    }


    // ============================================================
    // SAVE KUNDALI MILAN DETAILS
    // ============================================================

    private void saveKundaliMilanDetails(
            Order savedOrder,
            CheckoutRequest request
    ) {

        if (request.getServiceDetails() == null ||
                request.getServiceDetails().isEmpty()) {

            return;
        }


        for (CheckoutServiceDetailsRequest detailRequest :
                request.getServiceDetails()) {


            // ----------------------------------------------------
            // Find Product
            // ----------------------------------------------------

            Product product = productRepository
                    .findByProductIdAndActiveTrue(
                            detailRequest.getProductId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Active product not found with id: "
                                            + detailRequest.getProductId()
                            )
                    );


            // ----------------------------------------------------
            // Ignore non-service products
            // ----------------------------------------------------

            if (product.getType() !=
                    ProductType.SERVICE) {

                continue;
            }


            // ----------------------------------------------------
            // Find Service
            // ----------------------------------------------------

            Service service = serviceRepository
                    .findById(
                            product.getReferenceId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Service not found with id: "
                                            + product.getReferenceId()
                            )
                    );


            // ----------------------------------------------------
            // Only Kundali Milan needs details
            // ----------------------------------------------------

            if (service.getServiceType() !=
                    ServiceType.KUNDALI_MILAN) {

                continue;
            }


            // ----------------------------------------------------
            // Find matching OrderItem
            // ----------------------------------------------------

            OrderItem orderItem = savedOrder
                    .getItems()
                    .stream()
                    .filter(item ->
                            item.getProduct()
                                    .getProductId()
                                    .equals(
                                            product.getProductId()
                                    )
                    )
                    .findFirst()
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Order item not found for product: "
                                            + product.getProductId()
                            )
                    );


            // ----------------------------------------------------
            // Prevent duplicate details
            // ----------------------------------------------------

            if (kundaliMilanDetailsRepository
                    .existsByOrderItem_OrderItemId(
                            orderItem.getOrderItemId()
                    )) {

                throw new BadRequestException(
                        "Kundali Milan details already exist "
                                + "for this order item"
                );
            }


            // ----------------------------------------------------
            // Convert Request → Entity
            // ----------------------------------------------------

            KundaliMilanDetailsRequest kundaliRequest =
                    detailRequest.getKundaliMilan();

            KundaliMilanDetails details =
                    kundaliMilanDetailsTransformer
                            .toEntity(kundaliRequest);


            // ----------------------------------------------------
            // Link details to OrderItem
            // ----------------------------------------------------

            details.setOrderItem(orderItem);


            // ----------------------------------------------------
            // Save
            // ----------------------------------------------------

            kundaliMilanDetailsRepository.save(details);
        }
    }


    // ============================================================
    // GET MY ORDER
    // ============================================================

    @Transactional(readOnly = true)
    public OrderResponse getMyOrder(Long orderId) {

        Long userId = getAuthenticatedUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );


        // --------------------------------------------------------
        // SECURITY CHECK
        // --------------------------------------------------------

        if (!order.getUser()
                .getUserId()
                .equals(userId)) {

            throw new BadRequestException(
                    "You are not allowed to access this order"
            );
        }


        return orderTransformer.toResponse(order);
    }


    // ============================================================
    // GET MY ORDERS
    // ============================================================

    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(
            Pageable pageable
    ) {

        Long userId = getAuthenticatedUserId();

        return orderRepository
                .findByUser_UserId(
                        userId,
                        pageable
                )
                .map(orderTransformer::toResponse);
    }


    // ============================================================
    // GET AUTHENTICATED USER ID
    // ============================================================

    private Long getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new BadRequestException(
                    "User is not authenticated"
            );
        }


        Object principal =
                authentication.getPrincipal();


        if (!(principal instanceof AuthenticatedUser authenticatedUser)) {

            throw new BadRequestException(
                    "Invalid authenticated user"
            );
        }


        return authenticatedUser.getUserId();
    }


    // ============================================================
    // GENERATE ORDER NUMBER
    // ============================================================

    private String generateOrderNumber() {

        String date =
                LocalDate.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern("yyyyMMdd")
                        );

        String orderNumber;

        do {

            String randomPart =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .substring(0, 8)
                            .toUpperCase();

            orderNumber =
                    "NAD-" + date + "-" + randomPart;

        } while (
                orderRepository
                        .existsByOrderNumber(orderNumber)
        );

        return orderNumber;
    }
}