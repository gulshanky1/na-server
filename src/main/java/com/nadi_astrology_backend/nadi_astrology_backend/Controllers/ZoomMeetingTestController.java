package com.nadi_astrology_backend.nadi_astrology_backend.Controllers;

import com.nadi_astrology_backend.nadi_astrology_backend.Service.ZoomMeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/zoom-test")
@RequiredArgsConstructor
public class ZoomMeetingTestController {

    private final ZoomMeetingService zoomMeetingService;

    @PostMapping("/meeting")
    public Map<String, Object> createTestMeeting(
            @RequestParam String hostUserId
    ) {

        return zoomMeetingService.createMeeting(
                hostUserId,
                "Nadi Astrology - Zoom Integration Test",
                "Temporary meeting created to test Zoom API integration",
                "2026-09-20T17:00:00",
                120
        );
    }
}