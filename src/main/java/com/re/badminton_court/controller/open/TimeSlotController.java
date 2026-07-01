package com.re.badminton_court.controller.open;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.timeslot.TimeSlotResponse;
import com.re.badminton_court.service.timeslot.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/time-slots")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TimeSlotResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(timeSlotService.findActive(), "Get time slots successfully!!"));
    }
}
