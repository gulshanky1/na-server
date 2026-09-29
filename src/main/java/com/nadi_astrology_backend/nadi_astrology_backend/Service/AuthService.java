package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.GoogleAuthRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.LoginRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AuthResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.RefreshTokenResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.UnauthorizedException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.GoogleTokenService;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.GoogleUserInfo;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtService;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final EmailVerificationService emailVerificationService;

    private final StudentRepository studentRepository;

    private final UserTransformer userTransformer;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    private final CacheManager cacheManager;

    private final GoogleTokenService googleTokenService;


    /*
     * ============================================================
     * LOGIN
     * ============================================================
     */
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid email or password"
                                )
                        );

        if (!user.isAccountEnabled()) {

            throw new UnauthorizedException(
                    "Account is disabled"
            );
        }

        if (!user.isEmailVerified()) {

            throw new UnauthorizedException(
                    "Please verify your email before logging in"
            );
        }

        if (user.getPassword() == null) {

            throw new UnauthorizedException(
                    "This account uses Google login"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new UnauthorizedException(
                    "Invalid email or password"
            );
        }

        user.setLastLogin(
                LocalDateTime.now()
        );

        User savedUser =
                userRepository.save(user);

        var usersCache =
                cacheManager.getCache("users");

        if (usersCache != null) {

            usersCache.evict(
                    savedUser.getUserId()
            );
        }

        boolean student =
                studentRepository.existsByUser_UserId(
                        savedUser.getUserId()
                );

        String accessToken =
                jwtService.generateAccessToken(
                        savedUser
                );

        /*
         * New refresh-token family for this login session.
         */
        String tokenFamily =
                UUID.randomUUID().toString();

        String refreshToken =
                refreshTokenService.createRefreshToken(
                        savedUser,
                        tokenFamily
                );

        UserResponse userResponse =
                userTransformer.toResponse(
                        savedUser,
                        student
                );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
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

        if (request == null ||
                request.getIdToken() == null ||
                request.getIdToken().isBlank()) {

            throw new UnauthorizedException(
                    "Google authentication token is required"
            );
        }

        GoogleUserInfo googleUser =
                googleTokenService.verify(
                        request.getIdToken()
                );

        String email =
                googleUser.email();

        User user =
                userRepository.findByEmail(email)
                        .orElse(null);

        if (user == null) {

            user = User.builder()
                    .fullName(
                            googleUser.fullName()
                    )
                    .email(email)
                    .providerId(
                            googleUser.providerId()
                    )
                    .profileImage(
                            googleUser.profileImage()
                    )
                    .role(Role.USER)
                    .authProvider(AuthProvider.GOOGLE)
                    .emailVerified(true)
                    .accountEnabled(true)
                    .registeredAt(
                            LocalDateTime.now()
                    )
                    .updatedAt(
                            LocalDateTime.now()
                    )
                    .lastLogin(
                            LocalDateTime.now()
                    )
                    .build();

            user =
                    userRepository.save(user);

        } else {

            if (!user.isAccountEnabled()) {

                throw new UnauthorizedException(
                        "Account is disabled"
                );
            }

            if (user.getAuthProvider()
                    == AuthProvider.LOCAL) {

                throw new UnauthorizedException(
                        "An account already exists with this email. Please login using email and password."
                );
            }

            user.setFullName(
                    googleUser.fullName()
            );

            user.setProviderId(
                    googleUser.providerId()
            );

            user.setProfileImage(
                    googleUser.profileImage()
            );

            user.setEmailVerified(true);

            user.setLastLogin(
                    LocalDateTime.now()
            );

            user.setUpdatedAt(
                    LocalDateTime.now()
            );

            user =
                    userRepository.save(user);
        }

        var usersCache =
                cacheManager.getCache("users");

        if (usersCache != null) {

            usersCache.evict(
                    user.getUserId()
            );
        }

        boolean student =
                studentRepository.existsByUser_UserId(
                        user.getUserId()
                );

        String accessToken =
                jwtService.generateAccessToken(
                        user
                );

        /*
         * New refresh-token family for this login session.
         */
        String tokenFamily =
                UUID.randomUUID().toString();

        String refreshToken =
                refreshTokenService.createRefreshToken(
                        user,
                        tokenFamily
                );

        UserResponse userResponse =
                userTransformer.toResponse(
                        user,
                        student
                );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }


    /*
     * ============================================================
     * EMAIL VERIFICATION
     * ============================================================
     */
    public void verifyEmail(String token) {

        emailVerificationService.verifyEmail(
                token
        );
    }


    /*
     * ============================================================
     * RESEND VERIFICATION EMAIL
     * ============================================================
     */
    public void resendVerificationEmail(
            String email,
            String clientIp
    ) {

        emailVerificationService
                .resendVerificationEmail(
                        email,
                        clientIp
                );
    }


    /*
     * ============================================================
     * REFRESH ACCESS TOKEN
     * ============================================================
     */
    @Transactional
    public RefreshTokenResponse refreshAccessToken(
            String rawRefreshToken
    ) {

        RefreshTokenValidationResult result =
                refreshTokenService.validateAndRevoke(
                        rawRefreshToken
                );

        User user =
                result.getUser();

        String newAccessToken =
                jwtService.generateAccessToken(
                        user
                );

        /*
         * Keep the same token family during rotation.
         */
        String newRefreshToken =
                refreshTokenService.createRefreshToken(
                        user,
                        result.getTokenFamily()
                );

        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();
    }


    /*
     * ============================================================
     * LOGOUT
     * ============================================================
     */
    @Transactional
    public void logout(
            String rawRefreshToken
    ) {

        refreshTokenService.revokeToken(
                rawRefreshToken
        );
    }
}