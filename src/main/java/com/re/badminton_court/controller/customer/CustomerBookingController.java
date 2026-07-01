package com.re.badminton_court.controller.customer;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.booking.BookingRequest;
import com.re.badminton_court.model.dto.booking.BookingResponse;
import com.re.badminton_court.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/bookings")
public class CustomerBookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest request) {
        BookingResponse result = bookingService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Create booking successfully!!"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getMyBookings(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findMyBookings(pageable), "Get bookings successfully!!"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findMyBookingById(id), "Get booking successfully!!"));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.cancelMyBooking(id), "Cancel booking successfully!!"));
    }
}
