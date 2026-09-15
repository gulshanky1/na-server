package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.GoogleAuthRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.LoginRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AuthResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.UnauthorizedException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtService;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    private final UserTransformer userTransformer;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final CacheManager cacheManager;


    /*
     * ============================================================
     * LOGIN
     * ============================================================
     */
    public AuthResponse login(
            LoginRequest request
    ) {

        /*
         * Normalize email.
         */
        String email = request.getEmail()
                .trim()
                .toLowerCase();


        /*
         * Find User by email.
         */
        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid email or password"
                                )
                        );


        /*
         * Check whether account is enabled.
         */
        if (!user.isAccountEnabled()) {

            throw new UnauthorizedException(
                    "Account is disabled"
            );
        }


        /*
         * Google users don't have a local password.
         */
        if (user.getPassword() == null) {

            throw new UnauthorizedException(
                    "This account uses Google login"
            );
        }


        /*
         * Verify password.
         */
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new UnauthorizedException(
                    "Invalid email or password"
            );
        }


        /*
         * Update last login time.
         */
        user.setLastLogin(
                LocalDateTime.now()
        );


        /*
         * Save updated User.
         */
        User savedUser =
                userRepository.save(user);


        /*
         * ========================================================
         * CACHE INVALIDATION
         * ========================================================
         */

        var usersCache =
                cacheManager.getCache("users");

        if (usersCache != null) {

            usersCache.evict(
                    savedUser.getUserId()
            );
        }


        /*
         * ========================================================
         * CHECK STUDENT STATUS
         * ========================================================
         */

        boolean student =
                studentRepository.existsByUser_UserId(
                        savedUser.getUserId()
                );


        /*
         * Generate JWT access token.
         */
        String accessToken =
                jwtService.generateAccessToken(
                        savedUser
                );


        /*
         * Convert User -> UserResponse.
         */
        UserResponse userResponse =
                userTransformer.toResponse(
                        savedUser,
                        student
                );


        /*
         * Return authentication response.
         */
        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }


    /*
     * ============================================================
     * GOOGLE LOGIN
     * ============================================================
     */
    public AuthResponse googleLogin(
            GoogleAuthRequest request
    ) {

        /*
         * Validate request.
         */
        if (request == null) {

            throw new UnauthorizedException(
                    "Google authentication data is required"
            );
        }


        /*
         * Validate email.
         */
        if (request.getEmail() == null ||
                request.getEmail().trim().isEmpty()) {

            throw new UnauthorizedException(
                    "Google email is required"
            );
        }


        /*
         * Normalize email.
         */
        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();


        /*
         * Find existing user.
         */
        User user =
                userRepository.findByEmail(email)
                        .orElse(null);


        /*
         * ========================================================
         * CREATE NEW GOOGLE USER
         * ========================================================
         */
        if (user == null) {

            user = User.builder()
                    .fullName(request.getFullName())
                    .email(email)
                    .providerId(request.getProviderId())
                    .profileImage(request.getProfileImage())
                    .role(Role.USER)
                    .authProvider(AuthProvider.GOOGLE)
                    .emailVerified(true)
                    .accountEnabled(true)
                    .registeredAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .build();

            user =
                    userRepository.save(user);

        }

        /*
         * ========================================================
         * EXISTING USER
         * ========================================================
         */
        else {

            /*
             * Check account status.
             */
            if (!user.isAccountEnabled()) {

                throw new UnauthorizedException(
                        "Account is disabled"
                );
            }


            /*
             * Do not automatically convert a LOCAL account
             * into a Google account.
             */
            if (user.getAuthProvider() ==
                    AuthProvider.LOCAL) {

                throw new UnauthorizedException(
                        "An account already exists with this email. Please login using email and password."
                );
            }


            /*
             * Update Google account information.
             */
            user.setFullName(
                    request.getFullName()
            );

            user.setProviderId(
                    request.getProviderId()
            );

            user.setProfileImage(
                    request.getProfileImage()
            );

            user.setLastLogin(
                    LocalDateTime.now()
            );

            user.setUpdatedAt(
                    LocalDateTime.now()
            );


            /*
             * Save updated user.
             */
            user =
                    userRepository.save(user);
        }


        /*
         * ========================================================
         * CACHE INVALIDATION
         * ========================================================
         */

        var usersCache =
                cacheManager.getCache("users");

        if (usersCache != null) {

            usersCache.evict(
                    user.getUserId()
            );
        }


        /*
         * ========================================================
         * CHECK STUDENT STATUS
         * ========================================================
         */

        boolean student =
                studentRepository.existsByUser_UserId(
                        user.getUserId()
                );


        /*
         * ========================================================
         * GENERATE JWT
         * ========================================================
         */

        String accessToken =
                jwtService.generateAccessToken(
                        user
                );


        /*
         * ========================================================
         * USER RESPONSE
         * ========================================================
         */

        UserResponse userResponse =
                userTransformer.toResponse(
                        user,
                        student
                );


        /*
         * ========================================================
         * RETURN AUTH RESPONSE
         * ========================================================
         */

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }
}
