package com.qodesquare.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${KEYCLOAK_SERVER_URL:http://keycloak:8080}")
    private String serverUrl;

    @Value("${KEYCLOAK_REALM:timesheet}")
    private String realm;

    @Value("${KEYCLOAK_ADMIN:admin}")
    private String username;

    @Value("${KEYCLOAK_ADMIN_PASSWORD:admin}")
    private String password;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")  // Authenticate to master realm
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("admin-cli")
                .username(username)
                .password(password)
                .build();
    }
}
