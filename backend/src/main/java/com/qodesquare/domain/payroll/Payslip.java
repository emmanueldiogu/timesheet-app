package com.qodesquare.domain.payroll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.qodesquare.domain.employee.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payslips", indexes = @Index(name = "idx_payslips_employee_period", columnList = "employee, payrollPeriod"))
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payroll_period_id", nullable = false)
    private PayrollPeriod payrollPeriod;

    @Column(name = "gross_pay", nullable = false, precision = 10, scale = 2)
    private BigDecimal grossPay;

    @Column(name = "total_hours", precision = 6, scale = 2)
    private BigDecimal totalHours;

    @Column(name = "taxes", precision = 10, scale = 2)
    private BigDecimal taxes;

    @Column(name = "deductions", precision = 10, scale = 2)
    private BigDecimal deductions;

    @Column(name = "net_pay", nullable = false, precision = 10, scale = 2)
    private BigDecimal netPay;

    @Column(name = "details", columnDefinition = "jsonb")
    private String details; // store JSON as String, mapped to jsonb in DB

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusPayslip status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
