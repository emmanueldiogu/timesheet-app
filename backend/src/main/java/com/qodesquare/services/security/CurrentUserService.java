package com.qodesquare.services.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public String getCurrentKeycloakUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken auth) {
            Jwt jwt = auth.getToken();
            return jwt.getClaimAsString("sub");  // Keycloak user ID
        }
        return null;
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken auth) {
            Jwt jwt = auth.getToken();
            return jwt.getClaimAsString("email");
        }
        return null;
    }

    public String getCurrentUserFirstName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken auth) {
            Jwt jwt = auth.getToken();
            return jwt.getClaimAsString("given_name");
        }
        return null;
    }

    public String getCurrentUserLastName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken auth) {
            Jwt jwt = auth.getToken();
            return jwt.getClaimAsString("family_name");
        }
        return null;
    }

    public List<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken auth) {
            Jwt jwt = auth.getToken();
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof List<?> roles) {
                return roles.stream().map(Object::toString).toList();
            }
        }
        return List.of();
    }
}
