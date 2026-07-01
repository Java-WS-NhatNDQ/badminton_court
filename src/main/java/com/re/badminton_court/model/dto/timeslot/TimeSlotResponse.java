package com.re.badminton_court.model.dto.timeslot;

import java.time.LocalTime;

public record TimeSlotResponse(
        Long id,
        LocalTime startTime,
        LocalTime endTime,
        String label,
        Boolean isActive,
        Double basePrice
) {}
