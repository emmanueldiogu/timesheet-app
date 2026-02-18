package com.qodesquare.repositories.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qodesquare.domain.payroll.PayrollPeriod;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface PayrollPeriodRepository extends BaseRepository<PayrollPeriod, UUID> {
    
    List<PayrollPeriod> findByStartDateBetween(LocalDate start, LocalDate end);
    
    @Query("SELECT p FROM PayrollPeriod p WHERE p.status = 'OPEN' ORDER BY p.startDate DESC")
    List<PayrollPeriod> findOpenPeriods();
}

