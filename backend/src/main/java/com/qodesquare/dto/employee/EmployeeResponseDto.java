package com.qodesquare.dto.employee;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
) {}
