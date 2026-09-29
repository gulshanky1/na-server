package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOrderUserResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String profileImage;
}