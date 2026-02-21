package com.qodesquare.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.qodesquare.dto.time.TimeEntryResponse;
import com.qodesquare.services.time.TimeEntryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
@Slf4j
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @PostMapping("/clock-in")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    public TimeEntryResponse clockIn() {
        log.info("POST /api/time-entries/clock-in called");
        TimeEntryResponse response = timeEntryService.clockIn();
        log.info("Clock-in successful: {}", response.id());
        return response;
    }

    @PostMapping("/clock-out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public TimeEntryResponse clockOut() {
        log.info("POST /api/time-entries/clock-out called");
        TimeEntryResponse response = timeEntryService.clockOut();
        log.info("Clock-out successful: {}", response.id());
        return response;
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public List<TimeEntryResponse> listRecent() {
        log.info("GET /api/time-entries/recent called");
        List<TimeEntryResponse> entries = timeEntryService.listRecentForCurrentEmployee(10);
        log.info("Returning {} recent entries", entries.size());
        return entries;
    }
}
