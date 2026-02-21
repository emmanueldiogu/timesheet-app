package com.qodesquare.dto.time;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.qodesquare.domain.time.Source;
import com.qodesquare.domain.time.Status;

public record TimeEntryResponse(
    UUID id,
    UUID employeeId,
    String employeeName,
    LocalDate workDate,
    LocalDateTime clockIn,
    LocalDateTime clockOut,
    BigDecimal totalHours,
    Source source,
    Status status,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
