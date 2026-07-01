package com.re.badminton_court.model.dto.booking;

import com.re.badminton_court.model.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        LocalDate bookingDate,
        Long timeSlotId,
        String timeSlotLabel,
        Double totalPrice,
        BookingStatus status,
        LocalDateTime createdAt,
        Long userId,
        String username,
        Long courtId,
        String courtName,
        Long clusterId,
        String clusterName
) {}
