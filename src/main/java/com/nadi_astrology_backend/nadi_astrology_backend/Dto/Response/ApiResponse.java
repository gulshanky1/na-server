package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private boolean error;

    private int status;

    private String message;

    private T payload;
}
