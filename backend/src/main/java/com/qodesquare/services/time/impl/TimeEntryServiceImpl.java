package com.qodesquare.services.time.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.time.Source;
import com.qodesquare.domain.time.Status;
import com.qodesquare.domain.time.TimeEntry;
import com.qodesquare.dto.time.TimeEntryResponse;
import com.qodesquare.mappers.TimeEntryMapper;
import com.qodesquare.repositories.employee.EmployeeRepository;
import com.qodesquare.repositories.time.TimeEntryRepository;
import com.qodesquare.services.security.CurrentUserService;
import com.qodesquare.services.time.TimeEntryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeEntryServiceImpl implements TimeEntryService {

    private final TimeEntryRepository timeEntryRepository;
    private final EmployeeRepository employeeRepository;
    private final TimeEntryMapper timeEntryMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public TimeEntryResponse clockIn() {
        String keycloakUserId = currentUserService.getCurrentKeycloakUserId();
        Employee employee = employeeRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));

        log.info("Processing clock-in for employee: {}", employee.getId());

        LocalDate today = LocalDate.now();
        TimeEntry todayEntry = timeEntryRepository.findTopByEmployeeAndWorkDateOrderByCreatedAtDesc(employee, today)
                .orElse(null);

        // Check if already clocked in today
        if (todayEntry != null && todayEntry.getClockOut() == null) {
            throw new IllegalStateException("Already clocked in today at " + todayEntry.getClockIn());
        }

        LocalDateTime now = LocalDateTime.now();

        // Create new entry
        TimeEntry entry = TimeEntry.builder()
                .employee(employee)
                .workDate(today)
                .clockIn(now)
                .source(Source.AUTO)
                .status(Status.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();

        log.info("Creating new time entry for employee: {}", employee.getId());
        entry = timeEntryRepository.save(entry);

        log.info("Clock-in successful for employee: {} at {}", employee.getId(), now);
        return timeEntryMapper.toDto(entry);
    }

    @Override
    @Transactional
    public TimeEntryResponse clockOut() {
        String keycloakUserId = currentUserService.getCurrentKeycloakUserId();
        Employee employee = employeeRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));

        log.info("Processing clock-out for employee: {}", employee.getId());

        LocalDate today = LocalDate.now();
        TimeEntry entry = timeEntryRepository.findTopByEmployeeAndWorkDateOrderByCreatedAtDesc(employee, today)
                .orElseThrow(() -> new IllegalStateException("No active clock-in found for today"));

        if (entry.getClockOut() != null) {
            throw new IllegalStateException("Already clocked out");
        }

        LocalDateTime now = LocalDateTime.now();
        entry.setClockOut(now);

        // Calculate total hours
        Duration duration = Duration.between(entry.getClockIn(), entry.getClockOut());
        double totalHours = duration.toMinutes() / 60.0;
        entry.setTotalHours(BigDecimal.valueOf(totalHours).setScale(2, RoundingMode.HALF_UP));

        entry = timeEntryRepository.save(entry);
        log.info("Clock-out successful for employee: {} at {}", employee.getId(), now);

        return timeEntryMapper.toDto(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeEntryResponse> listRecentForCurrentEmployee(int limit) {
        String keycloakUserId = currentUserService.getCurrentKeycloakUserId();
        Employee employee = employeeRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));

        log.info("Fetching {} recent entries for employee: {}", limit, employee.getId());

        return timeEntryRepository.findByEmployeeOrderByWorkDateDescCreatedAtDesc(
                employee, PageRequest.of(0, limit))
                .stream()
                .map(timeEntryMapper::toDto)
                .toList();
    }
}
