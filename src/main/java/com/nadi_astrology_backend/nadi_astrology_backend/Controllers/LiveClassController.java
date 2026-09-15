package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;


import com.nadi_astrology_backend.nadi_astrology_backend.Dto.Response.StudentLiveClassResponse;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.AuthenticatedUser;
import com.nadi_astrology_backend.nadi_astrology_backend.Service.LiveClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/live-classes")
@RequiredArgsConstructor
public class LiveClassController {

    private final LiveClassService liveClassService;


    // =====================================================
    // STUDENT - MY LIVE CLASSES
    // =====================================================

    @GetMapping("/me")
    public List<StudentLiveClassResponse> getMyLiveClasses(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        return liveClassService.getMyLiveClasses(
                authenticatedUser.getUserId()
        );
    }
}