package com.re.badminton_court.controller.manager;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.booking.BookingResponse;
import com.re.badminton_court.service.booking.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManagerBookingController extends BaseManagerController {
    private final BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getBookings(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findManagerBookings(pageable), "Get bookings successfully!!"));
    }

    @PatchMapping("/bookings/{id}/confirm")
    public ResponseEntity<ApiResponse<BookingResponse>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.confirmByManager(id), "Confirm booking successfully!!"));
    }

    @PatchMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.cancelByManager(id), "Cancel booking successfully!!"));
    }

    @PatchMapping("/bookings/{id}/check-in")
    public ResponseEntity<ApiResponse<BookingResponse>> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.checkInByManager(id), "Check in booking successfully!!"));
    }
}
