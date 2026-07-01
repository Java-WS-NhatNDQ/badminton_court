package com.re.badminton_court.model.dto.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotNull
    private Long courtId;

    @NotNull
    private Long timeSlotId;

    @NotNull
    @FutureOrPresent
    private LocalDate bookingDate;
}
