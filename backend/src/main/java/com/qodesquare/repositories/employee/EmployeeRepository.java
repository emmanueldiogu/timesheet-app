package com.qodesquare.repositories.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.employee.Role;
import com.qodesquare.domain.employee.Status;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, UUID> {

    Optional<Employee> findByKeycloakUserId(String keycloakUserId);

    @Query("SELECT e FROM Employee e WHERE " +
            "(:status IS NULL OR e.status = :status)")
    org.springframework.data.domain.Page<Employee> search(@org.springframework.data.repository.query.Param("status") Status status, 
                                                       org.springframework.data.domain.Pageable pageable);

    List<Employee> findAllByStatus(Status status);
}
