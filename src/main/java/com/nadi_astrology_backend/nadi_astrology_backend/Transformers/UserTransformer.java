package com.nadi_astrology_backend.nadi_astrology_backend.Transformers;


import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request.UserRequest;
import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.UserResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer {

    // ==========================================
    // UserRequest -> User
    // ==========================================

    public User toEntity(UserRequest request) {

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        return user;
    }





    // ==========================================
    // User -> UserResponse
    // ==========================================

    public UserResponse toResponse(
            User user,
            boolean student
    ) {

        return UserResponse.builder()

                .userId(user.getUserId())

                .fullName(user.getFullName())

                .email(user.getEmail())

                .phone(user.getPhone())

                .role(user.getRole())

                .authProvider(user.getAuthProvider())

                .profileImage(user.getProfileImage())

                .emailVerified(user.isEmailVerified())

                .accountEnabled(user.isAccountEnabled())

                .registeredAt(user.getRegisteredAt())

                .updatedAt(user.getUpdatedAt())

                .lastLogin(user.getLastLogin())

                /*
                 * Derived student status
                 */
                .student(student)

                .build();
    }
}