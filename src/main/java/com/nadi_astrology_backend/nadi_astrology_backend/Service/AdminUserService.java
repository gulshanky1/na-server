package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.AccountStatusRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.StudentRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    private final UserTransformer userTransformer;

    private final CacheManager cacheManager;


    /*
     * ============================================================
     * GET ALL USERS
     * ============================================================
     */
    public Page<UserResponse> getAllUsers(
            Pageable pageable
    ) {

        return userRepository
                .findAll(pageable)
                .map(user -> {

                    /*
                     * Check whether this user is a Student.
                     */
                    boolean student =
                            studentRepository.existsByUser_UserId(
                                    user.getUserId()
                            );

                    /*
                     * Convert User -> UserResponse
                     */
                    return userTransformer.toResponse(
                            user,
                            student
                    );
                });
    }


    /*
     * ============================================================
     * GET USER BY ID
     * ============================================================
     */
    public UserResponse getUserById(
            Long userId
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        /*
         * Check Student status.
         */
        boolean student =
                studentRepository.existsByUser_UserId(
                        userId
                );


        /*
         * Convert User -> UserResponse
         */
        return userTransformer.toResponse(
                user,
                student
        );
    }


    /*
     * ============================================================
     * UPDATE ACCOUNT STATUS
     * ============================================================
     */
    @Transactional
    public UserResponse updateAccountStatus(
            Long userId,
            AccountStatusRequest request,
            Long adminUserId
    ) {

        /*
         * Prevent admin from disabling
         * their own account.
         */
        if (userId.equals(adminUserId)) {

            throw new BadRequestException(
                    "Admin cannot disable their own account"
            );
        }


        /*
         * Find User.
         */
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        /*
         * Update account status.
         */
        user.setAccountEnabled(
                request.getEnabled()
        );


        /*
         * Save User.
         */
        User savedUser =
                userRepository.save(user);


        /*
         * Remove old UserResponse from Redis.
         */
        evictUserCache(
                savedUser.getUserId()
        );


        /*
         * Check Student status.
         */
        boolean student =
                studentRepository.existsByUser_UserId(
                        savedUser.getUserId()
                );


        /*
         * Return updated response.
         */
        return userTransformer.toResponse(
                savedUser,
                student
        );
    }


    /*
     * ============================================================
     * EVICT USER CACHE
     * ============================================================
     */
    private void evictUserCache(
            Long userId
    ) {

        var usersCache =
                cacheManager.getCache("users");

        if (usersCache != null) {

            usersCache.evict(userId);
        }
    }
}