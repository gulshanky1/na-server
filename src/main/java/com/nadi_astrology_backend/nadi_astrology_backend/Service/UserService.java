package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    private final UserTransformer userTransformer;

    private final PasswordEncoder passwordEncoder;


    /*
     * ============================================================
     * REGISTER USER
     * ============================================================
     */
    public UserResponse registerUserByEmail(
            UserRequest request
    ) {

        /*
         * Normalize email
         *
         * Example:
         * Rahul@Gmail.com
         *       ↓
         * rahul@gmail.com
         */
        String email = request.getEmail()
                .trim()
                .toLowerCase();


        /*
         * Check duplicate email
         */
        if (userRepository.existsByEmail(email)) {

            throw new DuplicateResourceException(
                    "Email already registered"
            );
        }


        /*
         * Check duplicate phone
         */
        if (request.getPhone() != null
                && !request.getPhone().isBlank()
                && userRepository.existsByPhone(
                request.getPhone()
        )) {

            throw new DuplicateResourceException(
                    "Phone number already registered"
            );
        }


        /*
         * Convert DTO -> Entity
         */
        User user =
                userTransformer.toEntity(request);


        /*
         * Always USER during public registration.
         *
         * Client cannot send:
         *
         * "role": "ADMIN"
         *
         * and become an admin.
         */
        user.setEmail(email);

        user.setRole(Role.USER);


        /*
         * Encode password before saving.
         */
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        /*
         * Save User
         */
        User savedUser =
                userRepository.save(user);


        /*
         * Newly registered user is NOT a Student.
         *
         * Student will be created after successful
         * course purchase/payment.
         */
        boolean student = false;


        /*
         * Convert Entity -> Response
         */
        return userTransformer.toResponse(
                savedUser,
                student
        );
    }


    /*
     * ============================================================
     * GET USER BY ID
     * ============================================================
     */
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
         *
         * This is where student=true/false
         * is calculated.
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
}