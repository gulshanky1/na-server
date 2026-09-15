package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleAuthRequest {

    private String email;

    private String fullName;

    private String providerId;

    private String profileImage;
}