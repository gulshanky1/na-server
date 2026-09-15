package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;

import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserProfileRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserProfileResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Gender;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileTransformer {

    public UserProfile toEntity(
            UserProfileRequest request,
            User user
    ) {

        UserProfile profile = new UserProfile();

        profile.setUser(user);

        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setTimeOfBirth(request.getTimeOfBirth());
        profile.setPlaceOfBirth(request.getPlaceOfBirth());
        profile.setGender(Gender.valueOf(request.getGender()));
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setCountry(request.getCountry());
        profile.setPincode(request.getPincode());
        profile.setBio(request.getBio());
        profile.setProfileImage(request.getProfileImage());

        return profile;
    }

    public void updateEntity(
            UserProfile profile,
            UserProfileRequest request
    ) {

        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setTimeOfBirth(request.getTimeOfBirth());
        profile.setPlaceOfBirth(request.getPlaceOfBirth());
        profile.setGender(Gender.valueOf(request.getGender()));
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setCountry(request.getCountry());
        profile.setPincode(request.getPincode());
        profile.setBio(request.getBio());
        profile.setProfileImage(request.getProfileImage());
    }

    public UserProfileResponse toResponse(
            UserProfile profile
    ) {

        User user = profile.getUser();

        return UserProfileResponse.builder()
                .profileId(profile.getProfileId())
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .dateOfBirth(profile.getDateOfBirth())
                .timeOfBirth(profile.getTimeOfBirth())
                .placeOfBirth(profile.getPlaceOfBirth())
                .gender(String.valueOf(profile.getGender()))
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .country(profile.getCountry())
                .pincode(profile.getPincode())
                .bio(profile.getBio())
                .profileImage(profile.getProfileImage())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}