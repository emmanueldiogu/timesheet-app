package com.qodesquare.dto.employee;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.employee.Status;

public record EmployeeResponseDto(
    UUID id,
    String email,
    String firstName,
    String lastName,
    List<String> roles,
    Status status,
    LocalDate hireDate,
    BigDecimal baseSalary,
    BigDecimal hourlyRate,
    Instant createdAt,
    Instant updatedAt
) {
    public static EmployeeResponseDto fromEntity(Employee employee, String email, String firstName, String lastName, List<String> roles) {
        if (employee == null) {
            return null;
        }
        return new EmployeeResponseDto(
                employee.getId(),
                email,
                firstName,
                lastName,
                roles,
                employee.getStatus(),
                employee.getHireDate(),
                employee.getBaseSalary(),
                employee.getHourlyRate(),
                employee.getCreatedAt() != null ? employee.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant() : null,
                employee.getUpdatedAt() != null ? employee.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant() : null
        );
    }
}
