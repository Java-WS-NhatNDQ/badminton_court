package com.re.badminton_court.service.timeslot;

import com.re.badminton_court.model.dto.timeslot.TimeSlotRequest;
import com.re.badminton_court.model.dto.timeslot.TimeSlotResponse;

import java.util.List;

public interface TimeSlotService {
    List<TimeSlotResponse> findActive();
    List<TimeSlotResponse> findAll();
    TimeSlotResponse create(TimeSlotRequest request);
    TimeSlotResponse update(Long id, TimeSlotRequest request);
    void delete(Long id);
}
