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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Time Entries", description = "Time tracking and attendance management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @PostMapping("/clock-in")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Clock in", description = "Record clock-in time for the authenticated user. Creates a new time entry for today.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully clocked in"),
            @ApiResponse(responseCode = "409", description = "Already clocked in today"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    })
    public TimeEntryResponse clockIn() {
        log.info("POST /api/time-entries/clock-in called");
        TimeEntryResponse response = timeEntryService.clockIn();
        log.info("Clock-in successful: {}", response.id());
        return response;
    }

    @PostMapping("/clock-out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Clock out", description = "Record clock-out time for the authenticated user. Calculates total hours worked.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully clocked out"),
            @ApiResponse(responseCode = "409", description = "Already clocked out or no active clock-in found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing JWT token")
    })
    public TimeEntryResponse clockOut() {
        log.info("POST /api/time-entries/clock-out called");
        TimeEntryResponse response = timeEntryService.clockOut();
        log.info("Clock-out successful: {}", response.id());
        return response;
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @Operation(summary = "Get recent entries", description = "Retrieve the 10 most recent time entries for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent entries")
    public List<TimeEntryResponse> listRecent() {
        log.info("GET /api/time-entries/recent called");
        List<TimeEntryResponse> entries = timeEntryService.listRecentForCurrentEmployee(10);
        log.info("Returning {} recent entries", entries.size());
        return entries;
    }
}
