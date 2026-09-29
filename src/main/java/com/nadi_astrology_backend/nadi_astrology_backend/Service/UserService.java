package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.AdminUserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.DuplicateResourceException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final UserTransformer userTransformer;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;


    // ============================================================
    // REGISTER USER
    // ============================================================

    public UserResponse registerUserByEmail(
            UserRequest request
    ) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }

        if (request.getPhone() != null
                && !request.getPhone().isBlank()
                && userRepository.existsByPhone(
                request.getPhone()
        )) {

            throw new DuplicateResourceException(
                    "Phone number already registered"
            );
        }

        User user =
                userTransformer.toEntity(request);

        user.setEmail(email);

        user.setRole(Role.USER);

        user.setAuthProvider(
                com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider.LOCAL
        );

        user.setEmailVerified(false);

        user.setAccountEnabled(true);

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        User savedUser =
                userRepository.save(user);

        emailVerificationService
                .createAndSendVerificationToken(
                        savedUser
                );

        return userTransformer.toResponse(
                savedUser,
                false
        );
    }


    // ============================================================
    // GET USER BY ID
    // ============================================================

    @Cacheable(
            value = "users",
            key = "#userId"
    )
    public UserResponse getUserById(
            Long userId
    ) {

        /*
         * Find User
         */
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        /*
         * Check whether this User has
         * a Student record.
         */
        boolean student =
                studentRepository.existsByUser_UserId(
                        userId
                );


        /*
         * Convert Entity -> Response
         */
        return userTransformer.toResponse(
                user,
                student
        );
    }


    // ============================================================
    // ADMIN - GET ALL USERS
    // ============================================================

    public Page<AdminUserResponse> getAllUsers(
            String search,
            Role role,
            Boolean accountEnabled,
            Boolean student,
            Pageable pageable
    ) {

        String normalizedSearch =
                search != null && !search.isBlank()
                        ? search.trim()
                        : null;

        Page<User> users =
                userRepository.searchUsersWithFilters(
                        normalizedSearch,
                        role,
                        accountEnabled,
                        student,
                        pageable
                );

        return users.map(this::toAdminUserResponse);
    }


    // ============================================================
    // ADMIN - GET USER BY ID
    // ============================================================

    public AdminUserResponse getAdminUserById(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        return toAdminUserResponse(user);
    }


    // ============================================================
    // ADMIN - ENABLE / DISABLE USER ACCOUNT
    // ============================================================

    @Transactional
    public AdminUserResponse updateAccountStatus(
            Long userId,
            boolean enabled
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        /*
         * Update account status
         */
        user.setAccountEnabled(enabled);


        /*
         * Save updated user
         */
        User savedUser =
                userRepository.save(user);


        /*
         * Return updated admin response
         */
        return toAdminUserResponse(savedUser);
    }


    // ============================================================
    // ADMIN - ENTITY -> ADMIN RESPONSE
    // ============================================================

    private AdminUserResponse toAdminUserResponse(
            User user
    ) {

        /*
         * Check whether the user has
         * a Student record.
         */
        boolean student =
                studentRepository.existsByUser_UserId(
                        user.getUserId()
                );


        /*
         * Convert User -> AdminUserResponse
         */
        return AdminUserResponse.builder()

                .userId(
                        user.getUserId()
                )

                .fullName(
                        user.getFullName()
                )

                .email(
                        user.getEmail()
                )

                .phone(
                        user.getPhone()
                )

                .role(
                        user.getRole()
                )

                .authProvider(
                        user.getAuthProvider()
                )

                .profileImage(
                        user.getProfileImage()
                )

                .emailVerified(
                        user.isEmailVerified()
                )

                .accountEnabled(
                        user.isAccountEnabled()
                )

                .student(
                        student
                )

                .registeredAt(
                        user.getRegisteredAt()
                )

                .updatedAt(
                        user.getUpdatedAt()
                )

                .lastLogin(
                        user.getLastLogin()
                )

                .build();
    }
}