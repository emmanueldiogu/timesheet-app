package com.qodesquare.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.qodesquare.domain.time.TimeEntry;
import com.qodesquare.dto.time.TimeEntryResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TimeEntryMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", constant = "")
    TimeEntryResponse toDto(TimeEntry timeEntry);
}
