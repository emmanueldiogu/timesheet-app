package com.qodesquare.controllers;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/public/health")
    public Map<String, String> publicHealth() {
        return Map.of("status", "OK", "public", "accessible");
    }

    @GetMapping("/employee/profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public Map<String, String> employeeProfile() {
        return Map.of("message", "Employee profile", "user", "authenticated");
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminStats() {
        return Map.of("message", "Admin stats", "role", "ADMIN only");
    }
}

