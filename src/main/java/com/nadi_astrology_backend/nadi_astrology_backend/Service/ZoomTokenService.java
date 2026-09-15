package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Config.ZoomConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoomTokenService {

    private final ZoomConfig zoomConfig;

    private final RestClient restClient = RestClient.builder().build();

    public String getAccessToken() {

        String credentials =
                zoomConfig.getClientId()
                        + ":"
                        + zoomConfig.getClientSecret();

        String basicAuth = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "account_credentials");
        formData.add("account_id", zoomConfig.getAccountId());

        Map response = restClient.post()
                .uri(zoomConfig.getOauthUrl())
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Basic " + basicAuth
                )
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(Map.class);

        if (response == null || response.get("access_token") == null) {
            throw new IllegalStateException(
                    "Failed to obtain Zoom access token"
            );
        }

        return response.get("access_token").toString();
    }
}