package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.Order;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentEmailService {

    private final EmailService emailService;

    @Value("${app.admin.email}")
    private String adminEmail;


    // =====================================================
    // USER PAYMENT CONFIRMATION
    // =====================================================

    public void sendPaymentSuccessToUser(
            Payment payment
    ) {

        Order order =  payment.getOrder();

        String userEmail = order.getUser().getEmail();

        String userName =
                order.getUser().getFullName();

        String orderNumber =
                order.getOrderNumber();

        BigDecimal amount =
                payment.getAmount();

        String html = buildUserPaymentEmail(
                userName,
                orderNumber,
                amount
        );

        emailService.sendHtmlEmail(

                userEmail,

                "Payment Successful - Nadi Astrology",

                html
        );
    }


    // =====================================================
    // ADMIN PAYMENT NOTIFICATION
    // =====================================================

    public void sendPaymentSuccessToAdmin(
            Payment payment
    ) {

        Order order =
                payment.getOrder();

        String userName =
                order.getUser().getFullName();

        String userEmail =
                order.getUser().getEmail();

        String orderNumber =
                order.getOrderNumber();

        BigDecimal amount =
                payment.getAmount();

        String html = buildAdminPaymentEmail(

                userName,

                userEmail,

                orderNumber,

                amount
        );

        emailService.sendHtmlEmail(

                adminEmail,

                "New Payment Received - " + orderNumber,

                html
        );
    }


    // =====================================================
    // USER EMAIL TEMPLATE
    // =====================================================

    private String buildUserPaymentEmail(
            String userName,
            String orderNumber,
            BigDecimal amount
    ) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Payment Successful</title>
                </head>

                <body style="
                    margin:0;
                    padding:0;
                    background:#f5f5f5;
                    font-family:Arial,sans-serif;
                ">

                    <div style="
                        max-width:600px;
                        margin:30px auto;
                        background:#ffffff;
                        border-radius:10px;
                        overflow:hidden;
                        box-shadow:0 2px 10px rgba(0,0,0,0.08);
                    ">

                        <div style="
                            padding:25px;
                            background:#111827;
                            color:#ffffff;
                            text-align:center;
                        ">

                            <h1 style="
                                margin:0;
                                font-size:24px;
                            ">
                                Nadi Astrology
                            </h1>

                        </div>


                        <div style="padding:30px;">

                            <h2>
                                Payment Successful
                            </h2>

                            <p>
                                Dear %s,
                            </p>

                            <p>
                                Your payment has been successfully
                                verified.
                            </p>

                            <table style="
                                width:100%%;
                                border-collapse:collapse;
                                margin:20px 0;
                            ">

                                <tr>
                                    <td style="padding:10px 0;">
                                        <strong>Order Number</strong>
                                    </td>

                                    <td style="padding:10px 0;">
                                        %s
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:10px 0;">
                                        <strong>Amount</strong>
                                    </td>

                                    <td style="padding:10px 0;">
                                        ₹%s
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:10px 0;">
                                        <strong>Status</strong>
                                    </td>

                                    <td style="
                                        padding:10px 0;
                                        color:green;
                                    ">
                                        PAID
                                    </td>
                                </tr>

                            </table>


                            <p>
                                Thank you for your purchase.
                            </p>

                            <p>
                                Your order is now being processed.
                            </p>


                            <p style="
                                margin-top:30px;
                                color:#666666;
                            ">
                                Regards,<br>
                                Nadi Astrology Team
                            </p>

                        </div>

                    </div>

                </body>
                </html>
                """.formatted(
                userName,
                orderNumber,
                amount
        );
    }


    // =====================================================
    // ADMIN EMAIL TEMPLATE
    // =====================================================

    private String buildAdminPaymentEmail(
            String userName,
            String userEmail,
            String orderNumber,
            BigDecimal amount
    ) {

        return """
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>New Payment</title>
                </head>

                <body style="
                    margin:0;
                    padding:0;
                    background:#f5f5f5;
                    font-family:Arial,sans-serif;
                ">

                    <div style="
                        max-width:600px;
                        margin:30px auto;
                        background:#ffffff;
                        border-radius:10px;
                        overflow:hidden;
                    ">

                        <div style="
                            padding:25px;
                            background:#111827;
                            color:#ffffff;
                            text-align:center;
                        ">

                            <h1>
                                Nadi Astrology
                            </h1>

                        </div>


                        <div style="padding:30px;">

                            <h2>
                                New Payment Received
                            </h2>

                            <table style="
                                width:100%%;
                                border-collapse:collapse;
                            ">

                                <tr>
                                    <td style="padding:10px;">
                                        <strong>Customer</strong>
                                    </td>

                                    <td style="padding:10px;">
                                        %s
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:10px;">
                                        <strong>Email</strong>
                                    </td>

                                    <td style="padding:10px;">
                                        %s
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:10px;">
                                        <strong>Order</strong>
                                    </td>

                                    <td style="padding:10px;">
                                        %s
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:10px;">
                                        <strong>Amount</strong>
                                    </td>

                                    <td style="padding:10px;">
                                        ₹%s
                                    </td>
                                </tr>

                                <tr>
                                    <td style="padding:10px;">
                                        <strong>Status</strong>
                                    </td>

                                    <td style="
                                        padding:10px;
                                        color:green;
                                    ">
                                        PAID
                                    </td>
                                </tr>

                            </table>

                        </div>

                    </div>

                </body>

                </html>
                """.formatted(
                userName,
                userEmail,
                orderNumber,
                amount
        );
    }
}