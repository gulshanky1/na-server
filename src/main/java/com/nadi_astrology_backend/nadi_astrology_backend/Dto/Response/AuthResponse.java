package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private UserResponse user;
}