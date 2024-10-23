package com.yard.ms_security.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class OAuth2Service {

    @Value("${google.client.id}")
    private String googleClientID;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    @Value("${google.auth.uri")
    private String googleAuthUri;

    @Value("${google.token.uri}")
    private String googleTokenUri;

    @Value("$google.uri.info.uri")
    private String googleUserInfoUri;

    @Value("$github.client.id")
    private String githubClientId;


    private final RestTemplate restTemplate = new RestTemplate();

    public String getGoogleAuthUrl(String state) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(googleAuthUri)
                .queryParam("client_id", googleClientID)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .queryParam("state", state)
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent");
        return uriBuilder.toUriString();
    }
}
