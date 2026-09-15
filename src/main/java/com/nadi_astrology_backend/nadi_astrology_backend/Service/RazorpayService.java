package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.RazorpayOrderResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.PaymentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Payment;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.OrderRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.PaymentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    private final RazorpayClient razorpayClient;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;


    /**
     * Creates a Razorpay order for our internal order.
     */
    @Transactional
    public RazorpayOrderResponse createRazorpayOrder(Long orderId) {

        Long userId = getAuthenticatedUserId();

        /*
         * Find our internal order.
         */
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );


        /*
         * Make sure the logged-in user owns this order.
         */
        if (!order.getUser().getUserId().equals(userId)) {

            throw new BadRequestException(
                    "You are not allowed to make payment for this order"
            );
        }


        /*
         * Do not create another Razorpay order
         * for an already paid order.
         */
        if (order.getStatus() == OrderStatus.PAID) {

            throw new BadRequestException(
                    "Order has already been paid"
            );
        }


        /*
         * Only payment-pending orders can proceed.
         */
        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {

            throw new BadRequestException(
                    "Order is not available for payment. Current status: "
                            + order.getStatus()
            );
        }


        /*
         * Convert our INR amount to paise.
         *
         * Example:
         *
         * 45000.00 INR
         *
         * becomes
         *
         * 4500000 paise
         */
        long amountInPaise = order.getTotalAmount()
                .movePointRight(2)
                .setScale(0, RoundingMode.UNNECESSARY)
                .longValueExact();


        /*
         * Create Razorpay order request.
         */
        JSONObject razorpayOrderRequest = new JSONObject();

        razorpayOrderRequest.put(
                "amount",
                amountInPaise
        );

        razorpayOrderRequest.put(
                "currency",
                "INR"
        );

        razorpayOrderRequest.put(
                "receipt",
                order.getOrderNumber()
        );


        /*
         * Add useful internal information.
         *
         * Notes are visible in Razorpay's dashboard.
         */
        JSONObject notes = new JSONObject();

        notes.put(
                "internal_order_id",
                order.getOrderId()
        );

        notes.put(
                "order_number",
                order.getOrderNumber()
        );

        notes.put(
                "user_id",
                userId
        );

        razorpayOrderRequest.put(
                "notes",
                notes
        );


        try {

            /*
             * Call Razorpay Orders API.
             */
            com.razorpay.Order razorpayOrder =
                    razorpayClient.orders.create(
                            razorpayOrderRequest
                    );


            String razorpayOrderId =
                    razorpayOrder.get("id");


            /*
             * Create our payment record.
             */
            Payment payment = Payment.builder()
                    .order(order)
                    .razorpayOrderId(razorpayOrderId)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .status(PaymentStatus.CREATED)
                    .build();


            Payment savedPayment =
                    paymentRepository.save(payment);


            /*
             * Return only the information
             * required by our frontend.
             */
            return RazorpayOrderResponse.builder()
                    .paymentId(savedPayment.getPaymentId())
                    .orderId(order.getOrderId())
                    .orderNumber(order.getOrderNumber())
                    .razorpayOrderId(razorpayOrderId)
                    .amount(order.getTotalAmount())
                    .currency("INR")
                    .status(PaymentStatus.CREATED.name())
                    .build();


        } catch (RazorpayException e) {

            throw new BadRequestException(
                    "Unable to create Razorpay order: "
                            + e.getMessage()
            );
        }
    }


    /**
     * Gets the authenticated application's user ID.
     */
    private Long getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (authentication == null
                || !authentication.isAuthenticated()) {

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
}