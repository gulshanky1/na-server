package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private boolean success;
    private String message;
    private int status;
    private LocalDateTime timestamp;
}