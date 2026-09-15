package com.nadi_astrology_backend.nadi_astrology_backend.Dto.Request;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.ServiceFulfillmentStatus;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceFulfillmentUpdateRequest {

    // ============================================================
    // STATUS
    // ============================================================

    private ServiceFulfillmentStatus status;


    // ============================================================
    // REPORT
    // ============================================================

    /*
     * Report written by admin.
     *
     * TEXT can be large, so there is no small
     * validation limit here.
     */
    private String report;


    // ============================================================
    // ADMIN NOTES
    // ============================================================

    /*
     * Private notes for admin.
     */
    @Size(
            max = 10000,
            message = "Admin notes cannot exceed 10000 characters"
    )
    private String adminNotes;
}