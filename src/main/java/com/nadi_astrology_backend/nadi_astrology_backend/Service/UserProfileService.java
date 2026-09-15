package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserProfileRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserProfileResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ResourceNotFoundException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.UserProfile;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserProfileRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Transformers.UserProfileTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileTransformer userProfileTransformer;

    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(Long userId) {

        UserProfile profile =
                userProfileRepository
                        .findByUser_UserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User profile not found"
                                )
                        );

        return userProfileTransformer.toResponse(profile);
    }

    @Transactional
    public UserProfileResponse createOrUpdateProfile(
            Long userId,
            UserProfileRequest request
    ) {

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        UserProfile profile =
                userProfileRepository
                        .findByUser_UserId(userId)
                        .orElse(null);

        if (profile == null) {

            profile =
                    userProfileTransformer.toEntity(
                            request,
                            user
                    );

        } else {

            userProfileTransformer.updateEntity(
                    profile,
                    request
            );
        }

        UserProfile savedProfile =
                userProfileRepository.save(profile);

        return userProfileTransformer.toResponse(
                savedProfile
        );
    }
}