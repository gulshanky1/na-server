package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider;
import com.nadi_astrology_backend.nadi_astrology_backend.Enum.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long userId;

    private String fullName;

    private String email;

    private String phone;

    private Role role;

    private AuthProvider authProvider;

    private String profileImage;

    private boolean emailVerified;

    private boolean accountEnabled;

    private LocalDateTime registeredAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

    private boolean student;
}