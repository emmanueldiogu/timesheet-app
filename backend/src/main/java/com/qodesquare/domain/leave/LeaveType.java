package com.qodesquare.domain.leave;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "leave_types")
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", unique = true, nullable = false, length = 20)
    private Code code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "is_paid", nullable = false)
    private boolean isPaid;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
