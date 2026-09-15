package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test/email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;

    @PostMapping
    public String sendTestEmail(
            @RequestParam String email
    ) {

        emailService.sendHtmlEmail(
                email,
                "Nadi Astrology - Email Test",
                """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif;">

                    <h2>Nadi Astrology</h2>

                    <p>
                        Email service is working successfully.
                    </p>

                    <p>
                        JavaMailSender and Gmail SMTP are
                        configured correctly.
                    </p>

                    <p>
                        This is only a test email.
                    </p>

                </body>
                </html>
                """
        );

        return "Test email sent successfully";
    }
}