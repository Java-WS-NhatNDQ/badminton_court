package com.re.badminton_court.service.booking;

import com.re.badminton_court.model.dto.booking.BookingRequest;
import com.re.badminton_court.model.dto.booking.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {
    BookingResponse create(BookingRequest request);
    Page<BookingResponse> findMyBookings(Pageable pageable);
    BookingResponse findMyBookingById(Long id);
    BookingResponse cancelMyBooking(Long id);
    Page<BookingResponse> findAll(Pageable pageable);
    Page<BookingResponse> findManagerBookings(Pageable pageable);
    BookingResponse confirmByManager(Long id);
    BookingResponse cancelByManager(Long id);
    BookingResponse confirmByAdmin(Long id);
    BookingResponse cancelByAdmin(Long id);
    BookingResponse checkInByManager(Long id);
}
