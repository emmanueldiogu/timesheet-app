package com.qodesquare.repositories.leave;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.qodesquare.domain.leave.LeaveType;
import com.qodesquare.repositories.BaseRepository;

@Repository
public interface LeaveTypeRepository extends BaseRepository<LeaveType, UUID> {
}

