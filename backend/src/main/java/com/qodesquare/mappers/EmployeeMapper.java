package com.qodesquare.mappers;

import java.time.ZoneId;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.qodesquare.domain.employee.Employee;
import com.qodesquare.dto.employee.EmployeeResponseDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = ZoneId.class)
public interface EmployeeMapper {

    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "createdAt", expression = "java(employee.getCreatedAt() != null ? employee.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant() : null)")
    @Mapping(target = "updatedAt", expression = "java(employee.getUpdatedAt() != null ? employee.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant() : null)")
    EmployeeResponseDto toDto(Employee employee, String email, String firstName, String lastName, List<String> roles);
}
