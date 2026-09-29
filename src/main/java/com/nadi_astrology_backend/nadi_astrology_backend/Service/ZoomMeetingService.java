package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Config.ZoomConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoomMeetingService {

    private final ZoomConfig zoomConfig;
    private final ZoomTokenService zoomTokenService;

    private final RestClient restClient =
            RestClient.builder().build();


    // =====================================================
    // CREATE ZOOM MEETING
    // =====================================================

    public Map<String, Object> createMeeting(
            String hostUserId,
            String topic,
            String description,
            String startTime,
            int durationMinutes
    ) {

        String accessToken =
                zoomTokenService.getAccessToken();

        Map<String, Object> requestBody =
                Map.of(
                        "topic", topic,
                        "type", 2,
                        "start_time", startTime,
                        "duration", durationMinutes,
                        "timezone", "Asia/Kolkata",
                        "agenda",
                        description != null
                                ? description
                                : "",
                        "settings",
                        Map.of(
                                "join_before_host", false,
                                "waiting_room", true,
                                "mute_upon_entry", true,
                                "auto_recording", "none"
                        )
                );

        return restClient.post()
                .uri(
                        zoomConfig.getApiUrl()
                                + "/users/"
                                + hostUserId
                                + "/meetings"
                )
                .header(
                        "Authorization",
                        "Bearer " + accessToken
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(requestBody)
                .retrieve()
                .body(
                        new ParameterizedTypeReference<
                                Map<String, Object>
                                >() {}
                );
    }


    // =====================================================
    // UPDATE ZOOM MEETING
    // =====================================================

    public void updateMeeting(
            Long meetingId,
            String topic,
            String description,
            String startTime,
            int durationMinutes
    ) {

        String accessToken =
                zoomTokenService.getAccessToken();

        Map<String, Object> requestBody =
                Map.of(
                        "topic", topic,
                        "start_time", startTime,
                        "duration", durationMinutes,
                        "timezone", "Asia/Kolkata",
                        "agenda",
                        description != null
                                ? description
                                : ""
                );

        restClient.patch()
                .uri(
                        zoomConfig.getApiUrl()
                                + "/meetings/"
                                + meetingId
                )
                .header(
                        "Authorization",
                        "Bearer " + accessToken
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(requestBody)
                .retrieve()
                .toBodilessEntity();
    }


    // =====================================================
    // DELETE ZOOM MEETING
    // =====================================================

    public void deleteMeeting(
            Long meetingId
    ) {

        String accessToken =
                zoomTokenService.getAccessToken();

        restClient.delete()
                .uri(
                        zoomConfig.getApiUrl()
                                + "/meetings/"
                                + meetingId
                )
                .header(
                        "Authorization",
                        "Bearer " + accessToken
                )
                .retrieve()
                .toBodilessEntity();
    }
}