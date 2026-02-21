package com.qodesquare.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.employee.Role;
import com.qodesquare.domain.employee.Status;
import com.qodesquare.dto.employee.EmployeeResponseDto;
import com.qodesquare.exceptions.employee.EmployeeNotFoundException;
import com.qodesquare.services.employee.EmployeeService;
import com.qodesquare.services.security.CurrentUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CurrentUserService currentUserService;

    @GetMapping("/api/employees")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<EmployeeResponseDto> getAllEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Role role,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/employees called with search={}, status={}, role={}", search, status, role);
        Page<EmployeeResponseDto> result = employeeService.getAllEmployees(search, status, role, pageable);
        log.info("GET /api/employees returning {} results", result.getTotalElements());
        return result;
    }

    @GetMapping("/api/employees/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public EmployeeResponseDto getCurrentEmployee() {
        log.info("GET /api/employees/me called");
        
        String keycloakUserId = currentUserService.getCurrentKeycloakUserId();
        String email = currentUserService.getCurrentUserEmail();
        String firstName = currentUserService.getCurrentUserFirstName();
        String lastName = currentUserService.getCurrentUserLastName();
        List<String> roles = currentUserService.getCurrentUserRoles();

        log.info("Current Keycloak User ID: {}", keycloakUserId);
        
        if (keycloakUserId == null) {
            log.warn("Keycloak User ID is NULL");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        
        return employeeService.getOrCreateEmployee(keycloakUserId, email, firstName, lastName, roles);
    }
}
