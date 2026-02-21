package com.qodesquare.services.time;

import java.util.List;

import com.qodesquare.dto.time.TimeEntryResponse;

public interface TimeEntryService {
    
    TimeEntryResponse clockIn();
    
    TimeEntryResponse clockOut();
    
    List<TimeEntryResponse> listRecentForCurrentEmployee(int limit);
}
