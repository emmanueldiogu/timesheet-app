package com.qodesquare.services.employee.impl;

import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.employee.Role;
import com.qodesquare.domain.employee.Status;
import com.qodesquare.dto.employee.EmployeeResponseDto;
import com.qodesquare.mappers.EmployeeMapper;
import com.qodesquare.repositories.employee.EmployeeRepository;
import com.qodesquare.services.employee.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final Keycloak keycloak;
    private final EmployeeMapper employeeMapper;

    @Value("${KEYCLOAK_REALM:timesheet}")
    private String realm;

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> getAllEmployees(String search, Status status, Role role, Pageable pageable) {
        return employeeRepository.search(status, pageable)
                .map(this::mapToResponse);
    }

    @Override
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

        return employeeMapper.toDto(employee, email, firstName, lastName, roles);
    }

    private EmployeeResponseDto mapToResponse(Employee employee) {
        try {
            UserRepresentation user = keycloak.realm(realm).users().get(employee.getKeycloakUserId()).toRepresentation();
            List<String> roles = keycloak.realm(realm).users().get(employee.getKeycloakUserId()).roles().realmLevel().listAll()
                    .stream().map(r -> r.getName()).toList();

            return employeeMapper.toDto(employee, user.getEmail(), user.getFirstName(), user.getLastName(), roles);
        } catch (Exception e) {
            log.error("Error fetching Keycloak details for user: {}", employee.getKeycloakUserId(), e);
            return employeeMapper.toDto(employee, "ERROR", "ERROR", "ERROR", List.of());
        }
    }
}
