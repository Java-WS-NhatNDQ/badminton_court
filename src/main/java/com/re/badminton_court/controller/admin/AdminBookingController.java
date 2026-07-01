package com.re.badminton_court.controller.admin;

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
public class AdminBookingController extends BaseAdminController {
    private final BookingService bookingService;

    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findAll(pageable), "Get bookings successfully!!"));
    }

    @PatchMapping("/bookings/{id}/confirm")
    public ResponseEntity<ApiResponse<BookingResponse>> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.confirmByAdmin(id), "Confirm booking successfully!!"));
    }

    @PatchMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.cancelByAdmin(id), "Cancel booking successfully!!"));
    }
}
