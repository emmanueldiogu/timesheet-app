package com.qodesquare.controllers;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/public/health")
    public Map<String, String> publicHealth() {
        return Map.of("status", "OK", "public", "accessible");
    }

    @GetMapping("/api/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminStats() {
        return Map.of("message", "Admin stats", "role", "ADMIN only");
    }
}

