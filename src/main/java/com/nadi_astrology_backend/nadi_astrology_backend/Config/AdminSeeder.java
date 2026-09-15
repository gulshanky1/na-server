package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = "umangbezel@gmail.com";

        if (userRepository.findByEmail(adminEmail).isPresent()) {

            System.out.println(
                    "Admin already exists: " + adminEmail
            );

            return;
        }

        User admin = User.builder()
                .fullName("Umang Taneja")
                .email(adminEmail)
                .password(
                        passwordEncoder.encode("Umang@123")
                )
                .role(Role.ADMIN)
                .authProvider(AuthProvider.LOCAL)
                .emailVerified(true)
                .accountEnabled(true)
                .build();

        userRepository.save(admin);

        System.out.println(
                "======================================"
        );
        System.out.println(
                "ADMIN USER CREATED"
        );
        System.out.println(
                "Email: " + adminEmail
        );
        System.out.println(
                "======================================"
        );
    }
}