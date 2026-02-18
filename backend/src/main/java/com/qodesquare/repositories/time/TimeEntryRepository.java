package com.qodesquare.repositories.time;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.time.TimeEntry;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface TimeEntryRepository extends BaseRepository<TimeEntry, UUID> {

    List<TimeEntry> findByEmployeeAndWorkDateBetween(Employee employee, LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(t.totalHours), 0) FROM TimeEntry t " +
            "WHERE t.employee = :employee AND t.workDate BETWEEN :start AND :end " +
            "AND t.status = com.qodesquare.domain.time.TimeEntry.Status.APPROVED")
    BigDecimal totalApprovedHours(Employee employee, LocalDate start, LocalDate end);
}
