package com.nadi_astrology_backend.nadi_astrology_backend.Security;

public record GoogleUserInfo(
        String email,
        String providerId,
        String fullName,
        String profileImage
) {
}