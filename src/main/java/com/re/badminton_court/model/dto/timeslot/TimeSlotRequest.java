package com.re.badminton_court.model.dto.timeslot;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotRequest {
    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private String label;

    @NotNull
    private Double basePrice;

    private Boolean isActive;
}
