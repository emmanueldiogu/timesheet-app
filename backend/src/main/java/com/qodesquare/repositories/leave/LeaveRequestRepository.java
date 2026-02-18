package com.qodesquare.repositories.leave;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.leave.LeaveRequest;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface LeaveRequestRepository extends BaseRepository<LeaveRequest, UUID> {
    
    List<LeaveRequest> findByEmployeeAndStatus(Employee employee, LeaveRequest.Status status);
    
    List<LeaveRequest> findByStartDateBetweenAndStatus(LocalDate start, LocalDate end, LeaveRequest.Status status);
}

