package com.re.badminton_court.controller.admin;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.timeslot.TimeSlotRequest;
import com.re.badminton_court.model.dto.timeslot.TimeSlotResponse;
import com.re.badminton_court.service.timeslot.TimeSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminTimeSlotController extends BaseAdminController {
    private final TimeSlotService timeSlotService;

    @GetMapping("/time-slots")
    public ResponseEntity<ApiResponse<List<TimeSlotResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(timeSlotService.findAll(), "Get time slots successfully!!"));
    }

    @PostMapping("/time-slots")
    public ResponseEntity<ApiResponse<TimeSlotResponse>> create(@Valid @RequestBody TimeSlotRequest request) {
        TimeSlotResponse result = timeSlotService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Create time slot successfully!!"));
    }

    @PutMapping("/time-slots/{id}")
    public ResponseEntity<ApiResponse<TimeSlotResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TimeSlotRequest request) {
        return ResponseEntity.ok(ApiResponse.success(timeSlotService.update(id, request), "Update time slot successfully!!"));
    }

    @DeleteMapping("/time-slots/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        timeSlotService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete time slot successfully!!"));
    }
}
