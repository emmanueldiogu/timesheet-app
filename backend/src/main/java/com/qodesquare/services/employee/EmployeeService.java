package com.qodesquare.services.employee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.qodesquare.domain.employee.Role;
import com.qodesquare.domain.employee.Status;
import com.qodesquare.dto.employee.EmployeeResponseDto;

public interface EmployeeService {

    Page<EmployeeResponseDto> getAllEmployees(String search, Status status, Role role, Pageable pageable);

    EmployeeResponseDto getOrCreateEmployee(String keycloakId, String email, String firstName, String lastName, List<String> roles);
}
