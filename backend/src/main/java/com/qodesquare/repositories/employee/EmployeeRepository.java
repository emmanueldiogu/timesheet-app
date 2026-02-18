package com.qodesquare.repositories.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.domain.employee.Role;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, UUID> {

    Optional<Employee> findByKeycloakUserId(String keycloakUserId);

    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Employee> search(String search);

    List<Employee> findAllByRole(Role role);
}
