package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.NotificationType;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ProductType;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Book;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.OrderItem;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Payment;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Product;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.BookRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFulfillmentService {

    private final CourseEnrollmentService courseEnrollmentService;
    private final PaymentEmailService paymentEmailService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ServiceFulfillmentService serviceFulfillmentService;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Transactional
    public void fulfillSuccessfulPayment(Payment payment) {

        if (payment == null) {
            throw new IllegalArgumentException("Payment is required");
        }

        Order order = payment.getOrder();

        if (order == null) {
            throw new IllegalStateException("Payment order is missing");
        }

        User customer = order.getUser();

        if (customer == null) {
            throw new IllegalStateException("Order user is missing");
        }

        Long userId = customer.getUserId();

        /*
         * ---------------------------------------------------------
         * IDEMPOTENCY CHECK
         * ---------------------------------------------------------
         *
         * If payment fulfillment was already completed,
         * we only check SERVICE items.
         *
         * This allows older successful payments to receive
         * their missing ServiceFulfillment records.
         *
         * We do NOT repeat:
         * - course enrollment
         * - book stock reduction
         * - payment notification
         * - admin notification
         * - customer email
         * - admin email
         */
        if (payment.isFulfillmentCompleted()) {

            createMissingServiceFulfillments(order);

            return;
        }

        /*
         * ---------------------------------------------------------
         * 1. PRODUCT FULFILLMENT
         * ---------------------------------------------------------
         */

        for (OrderItem item : order.getItems()) {

            Product product = item.getProduct();

            if (product == null) {
                continue;
            }

            if (product.getType() == ProductType.COURSE) {

                Long courseId = product.getReferenceId();

                courseEnrollmentService.createEnrollment(
                        userId,
                        courseId,
                        order.getOrderId()
                );

                notificationService.createNotification(
                        userId,
                        NotificationType.COURSE_ENROLLMENT,
                        "Course Enrollment Successful",
                        "You have been successfully enrolled in the course: "
                                + product.getName(),
                        courseId,
                        "COURSE"
                );

            } else if (product.getType() == ProductType.BOOK) {

                /*
                 * -------------------------------------------------
                 * BOOK STOCK FULFILLMENT
                 * -------------------------------------------------
                 *
                 * PESSIMISTIC_WRITE lock prevents two concurrent
                 * payments from modifying the same book stock at
                 * the same time.
                 */

                Long bookId = product.getReferenceId();

                Book book = bookRepository
                        .findByIdForUpdate(bookId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Book not found with id: " + bookId
                                )
                        );

                Integer quantity = item.getQuantity();

                if (quantity == null || quantity <= 0) {
                    throw new BadRequestException(
                            "Invalid book quantity"
                    );
                }

                if (!book.isActive()) {
                    throw new BadRequestException(
                            "Book is no longer available"
                    );
                }

                if (book.getStockQuantity() == null) {
                    throw new BadRequestException(
                            "Book stock is not configured"
                    );
                }

                if (book.getStockQuantity() < quantity) {
                    throw new BadRequestException(
                            "Insufficient stock for book: "
                                    + book.getTitle()
                    );
                }

                book.setStockQuantity(
                        book.getStockQuantity() - quantity
                );

                bookRepository.save(book);

            } else if (product.getType() == ProductType.SERVICE) {

                /*
                 * Create service fulfillment.
                 *
                 * This automatically supports:
                 *
                 * STANDARD
                 * KUNDALI_MILAN
                 *
                 * ServiceFulfillmentService gets the actual
                 * ServiceType from the purchased Service.
                 */
                serviceFulfillmentService.createFulfillment(
                        item.getOrderItemId()
                );

            } else if(product.getType() == ProductType.PHONE_CONSULTATION) {

                /*
                 * Consultation fulfillment will be implemented later.
                 */
            }
        }

        /*
         * ---------------------------------------------------------
         * 2. CUSTOMER PAYMENT NOTIFICATION
         * ---------------------------------------------------------
         */

        notificationService.createNotification(
                userId,
                NotificationType.PAYMENT_SUCCESS,
                "Payment Successful",
                "Your payment for order "
                        + order.getOrderNumber()
                        + " has been successfully verified.",
                order.getOrderId(),
                "ORDER"
        );

        /*
         * ---------------------------------------------------------
         * 3. ADMIN DASHBOARD NOTIFICATION
         * ---------------------------------------------------------
         */

        userRepository.findByEmail(adminEmail)
                .ifPresent(admin -> {

                    notificationService.createNotification(
                            admin.getUserId(),
                            NotificationType.PAYMENT_SUCCESS,
                            "New Payment Received",
                            "Payment received from "
                                    + customer.getFullName()
                                    + " for order "
                                    + order.getOrderNumber()
                                    + ".",
                            order.getOrderId(),
                            "ORDER"
                    );
                });

        /*
         * ---------------------------------------------------------
         * 4. CUSTOMER EMAIL
         * ---------------------------------------------------------
         *
         * Email failure should NOT rollback the payment.
         */

        try {

            paymentEmailService.sendPaymentSuccessToUser(payment);

        } catch (Exception e) {

            System.err.println(
                    "Failed to send payment success email to customer: "
                            + customer.getEmail()
            );

            e.printStackTrace();
        }

        /*
         * ---------------------------------------------------------
         * 5. ADMIN EMAIL
         * ---------------------------------------------------------
         *
         * Email failure should NOT rollback the payment.
         */

        try {

            paymentEmailService.sendPaymentSuccessToAdmin(payment);

        } catch (Exception e) {

            System.err.println(
                    "Failed to send payment notification email to admin: "
                            + adminEmail
            );

            e.printStackTrace();
        }

        /*
         * ---------------------------------------------------------
         * 6. MARK FULFILLMENT AS COMPLETED
         * ---------------------------------------------------------
         */

        payment.setFulfillmentCompleted(true);
    }

    /*
     * -------------------------------------------------------------
     * CREATE MISSING SERVICE FULFILLMENTS
     * -------------------------------------------------------------
     *
     * Used for payments that were already marked as fulfilled
     * before ServiceFulfillment was implemented.
     *
     * createFulfillment() is idempotent, so calling it again for
     * an existing fulfillment is safe.
     */
    private void createMissingServiceFulfillments(Order order) {

        if (order.getItems() == null) {
            return;
        }

        for (OrderItem item : order.getItems()) {

            if (item == null) {
                continue;
            }

            Product product = item.getProduct();

            if (product == null) {
                continue;
            }

            if (product.getType() == ProductType.SERVICE) {

                serviceFulfillmentService.createFulfillment(
                        item.getOrderItemId()
                );
            }
        }
    }
}