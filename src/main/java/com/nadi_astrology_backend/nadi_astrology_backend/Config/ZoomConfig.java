package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ZoomConfig {

    @Value("${zoom.account-id}")
    private String accountId;

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    @Value("${zoom.oauth-url}")
    private String oauthUrl;

    @Value("${zoom.api-url}")
    private String apiUrl;

    @Value("${zoom.host-user-id}")
    private String hostUserId;
}