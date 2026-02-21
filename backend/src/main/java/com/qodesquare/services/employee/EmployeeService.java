package com.qodesquare.services.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.employee.Role;
import com.qodesquare.domain.employee.Status;
import com.qodesquare.dto.employee.EmployeeResponseDto;
import com.qodesquare.repositories.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Keycloak keycloak;

    @org.springframework.beans.factory.annotation.Value("${KEYCLOAK_REALM:timesheet}")
    private String realm;

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(String search, Status status, Role role, Pageable pageable) {
        return employeeRepository.search(status, pageable)
                .map(this::mapToResponse);
    }

    private EmployeeResponseDto mapToResponse(Employee employee) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(employee.getKeycloakUserId()).toRepresentation();
            List<String> roles = keycloak.realm(realm).users().get(employee.getKeycloakUserId()).roles().realmLevel().listAll()
                    .stream().map(r -> r.getName()).toList();

            return EmployeeResponseDto.fromEntity(employee, user.getEmail(), user.getFirstName(), user.getLastName(), roles);
        } catch (Exception e) {
            log.error("Error fetching Keycloak details for user: {}", employee.getKeycloakUserId(), e);
            return EmployeeResponseDto.fromEntity(employee, "ERROR", "ERROR", "ERROR", List.of());
        }
    }

    @Transactional
    public EmployeeResponseDto getOrCreateEmployee(String keycloakId, String email, String firstName, String lastName, List<String> roles) {
        Employee employee = employeeRepository.findByKeycloakUserId(keycloakId)
                .orElseGet(() -> {
                    log.info("Creating new domain profile for Keycloak user: {}", keycloakId);
                    return employeeRepository.save(Employee.builder()
                            .keycloakUserId(keycloakId)
                            .status(Status.ACTIVE)
                            .build());
                });

        return EmployeeResponseDto.fromEntity(employee, email, firstName, lastName, roles);
    }
}
