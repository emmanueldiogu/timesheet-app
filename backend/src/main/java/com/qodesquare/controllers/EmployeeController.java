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

import com.qodesquare.domain.employee.Role;
import com.qodesquare.domain.employee.Status;
import com.qodesquare.dto.employee.EmployeeResponseDto;
import com.qodesquare.services.employee.EmployeeService;
import com.qodesquare.services.security.CurrentUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/employees")
@Tag(name = "Employees", description = "Employee management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CurrentUserService currentUserService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all employees", description = "Retrieve paginated list of employees with optional filters. Admin only.")
    public Page<EmployeeResponseDto> getAllEmployees(
            @Parameter(description = "Search term for filtering employees") @RequestParam(required = false) String search,
            @Parameter(description = "Filter by employee status") @RequestParam(required = false) Status status,
            @Parameter(description = "Filter by employee role") @RequestParam(required = false) Role role,
            @Parameter(description = "Pagination: page number (0-based)", example = "0") @PageableDefault(size = 20) Pageable pageable) {
        log.info("GET /api/employees called with search={}, status={}, role={}", search, status, role);
        Page<EmployeeResponseDto> result = employeeService.getAllEmployees(search, status, role, pageable);
        log.info("GET /api/employees returning {} results", result.getTotalElements());
        return result;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get current employee", description = "Get the authenticated user's employee profile. Creates profile if not exists.")
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
