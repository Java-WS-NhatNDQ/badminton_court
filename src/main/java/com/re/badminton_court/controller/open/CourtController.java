package com.re.badminton_court.controller.open;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtResponse;
import com.re.badminton_court.service.cluster_and_court.BadmintonCourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CourtController {
    private final BadmintonCourtService courtService;

    @GetMapping("/courts")
    public ResponseEntity<ApiResponse<Page<BadmintonCourtResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findAll(pageable), "Get courts successfully!!"));
    }

    @GetMapping("/courts/{id}")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findById(id), "Get court successfully!!"));
    }

    @GetMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<ApiResponse<Page<BadmintonCourtResponse>>> getByClusterId(
            @PathVariable Long clusterId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findByCluster(clusterId, pageable), "Get courts successfully!!"));
    }

    @GetMapping("/courts/available")
    public ResponseEntity<ApiResponse<List<BadmintonCourtResponse>>> getAvailable(
            @RequestParam(required = false) Long clusterId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long timeSlotId) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findAvailableCourts(clusterId, date, timeSlotId), "Get available courts successfully!!"));
    }
}
