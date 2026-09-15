package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.PaymentVerifyRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.PaymentVerifyResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.OrderStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.PaymentStatus;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Payment;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.PaymentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentFulfillmentService paymentFulfillmentService;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;


    // ============================================================
    // VERIFY PAYMENT
    // ============================================================

    @Transactional
    public PaymentVerifyResponse verifyPayment(
            PaymentVerifyRequest request
    ) {

        Long userId = getAuthenticatedUserId();


        // ========================================================
        // VALIDATE REQUEST
        // ========================================================

        if (request == null) {

            throw new BadRequestException(
                    "Payment verification request is required"
            );
        }


        // ========================================================
        // FIND PAYMENT
        // ========================================================

        /*
         * Find our payment using Razorpay Order ID.
         *
         * IMPORTANT:
         * This is only used to locate our database record.
         */
        Payment payment =
                paymentRepository
                        .findByRazorpayOrderId(
                                request.getRazorpayOrderId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found for Razorpay order: "
                                                + request.getRazorpayOrderId()
                                )
                        );


        Order order = payment.getOrder();


        // ========================================================
        // VERIFY OWNERSHIP
        // ========================================================

        /*
         * Make sure this order belongs to
         * the currently authenticated user.
         */
        if (!order.getUser().getUserId().equals(userId)) {

            throw new BadRequestException(
                    "You are not allowed to verify this payment"
            );
        }


        // ========================================================
        // ALREADY SUCCESSFUL
        // ========================================================

        /*
         * If payment was already successfully verified,
         * run fulfillment again safely.
         *
         * PaymentFulfillmentService is idempotent.
         *
         * This is also useful for older successful payments
         * that were verified before fulfillment was implemented.
         */
        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            paymentFulfillmentService.fulfillSuccessfulPayment(
                    payment
            );

            return buildResponse(
                    payment,
                    "Payment has already been verified successfully"
            );
        }


        // ========================================================
        // RAZORPAY INFORMATION
        // ========================================================

        /*
         * IMPORTANT SECURITY RULE:
         *
         * Do NOT trust the Razorpay order ID
         * sent by the frontend for signature generation.
         *
         * Use the Razorpay order ID stored in our database.
         */
        String serverRazorpayOrderId =
                payment.getRazorpayOrderId();


        String razorpayPaymentId =
                request.getRazorpayPaymentId();


        String razorpaySignature =
                request.getRazorpaySignature();


        // ========================================================
        // VERIFY RAZORPAY SIGNATURE
        // ========================================================

        boolean signatureValid;

        try {

            JSONObject attributes =
                    new JSONObject();


            attributes.put(
                    "razorpay_order_id",
                    serverRazorpayOrderId
            );


            attributes.put(
                    "razorpay_payment_id",
                    razorpayPaymentId
            );


            attributes.put(
                    "razorpay_signature",
                    razorpaySignature
            );


            signatureValid =
                    Utils.verifyPaymentSignature(
                            attributes,
                            razorpayKeySecret
                    );

        } catch (Exception e) {

            throw new BadRequestException(
                    "Unable to verify Razorpay payment signature"
            );
        }


        // ========================================================
        // INVALID SIGNATURE
        // ========================================================

        if (!signatureValid) {

            payment.setStatus(
                    PaymentStatus.FAILED
            );


            payment.setErrorCode(
                    "INVALID_SIGNATURE"
            );


            payment.setErrorDescription(
                    "Razorpay payment signature verification failed"
            );


            paymentRepository.save(payment);


            throw new BadRequestException(
                    "Invalid Razorpay payment signature"
            );
        }


        // ========================================================
        // PREVENT DUPLICATE RAZORPAY PAYMENT ID
        // ========================================================

        /*
         * Prevent the same Razorpay Payment ID
         * from being attached to another payment.
         */
        paymentRepository
                .findByRazorpayPaymentId(
                        razorpayPaymentId
                )
                .ifPresent(existingPayment -> {

                    if (!existingPayment
                            .getPaymentId()
                            .equals(payment.getPaymentId())) {

                        throw new BadRequestException(
                                "Razorpay payment has already been used"
                        );
                    }
                });


        // ========================================================
        // STORE SUCCESSFUL PAYMENT
        // ========================================================

        payment.setRazorpayPaymentId(
                razorpayPaymentId
        );


        payment.setRazorpaySignature(
                razorpaySignature
        );


        payment.setStatus(
                PaymentStatus.SUCCESS
        );


        payment.setErrorCode(null);

        payment.setErrorDescription(null);


        paymentRepository.save(payment);


        // ========================================================
        // UPDATE ORDER
        // ========================================================

        /*
         * Payment is successfully verified,
         * therefore our internal order becomes PAID.
         */
        order.setStatus(
                OrderStatus.PAID
        );


        /*
         * Order is already attached to Payment,
         * so JPA will persist the status change
         * inside this transaction.
         */


        // ========================================================
        // FULFILL PAYMENT
        // ========================================================

        /*
         * Process everything purchased in this order.
         *
         * COURSE:
         *     Create Student if required
         *     Create CourseEnrollment
         *
         * BOOK:
         *     Will be implemented later
         *
         * SERVICE:
         *     Will be implemented later
         *
         * PHONE CONSULTATION:
         *     Will be implemented later
         */
        paymentFulfillmentService.fulfillSuccessfulPayment(
                payment
        );


        // ========================================================
        // RESPONSE
        // ========================================================

        return buildResponse(
                payment,
                "Payment verified successfully"
        );
    }


    // ============================================================
    // BUILD RESPONSE
    // ============================================================

    private PaymentVerifyResponse buildResponse(
            Payment payment,
            String message
    ) {

        Order order =
                payment.getOrder();


        return PaymentVerifyResponse.builder()

                .paymentId(
                        payment.getPaymentId()
                )

                .orderId(
                        order.getOrderId()
                )

                .orderNumber(
                        order.getOrderNumber()
                )

                .razorpayOrderId(
                        payment.getRazorpayOrderId()
                )

                .razorpayPaymentId(
                        payment.getRazorpayPaymentId()
                )

                .amount(
                        payment.getAmount()
                )

                .currency(
                        payment.getCurrency()
                )

                .paymentStatus(
                        payment.getStatus()
                )

                .orderStatus(
                        order.getStatus()
                )

                .message(
                        message
                )

                .build();
    }


    // ============================================================
    // GET AUTHENTICATED USER ID
    // ============================================================

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


        if (!(principal
                instanceof AuthenticatedUser authenticatedUser)) {

            throw new BadRequestException(
                    "Invalid authenticated user"
            );
        }


        return authenticatedUser.getUserId();
    }
}