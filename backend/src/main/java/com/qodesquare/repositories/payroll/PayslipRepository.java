package com.qodesquare.repositories.payroll;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.payroll.Payslip;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface PayslipRepository extends BaseRepository<Payslip, UUID> {
    
    List<Payslip> findByEmployeeOrderByCreatedAtDesc(Employee employee);
}

