package com.re.badminton_court.controller.admin;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtRequest;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtResponse;
import com.re.badminton_court.model.dto.badminton_court.CourtAvailabilityRequest;
import com.re.badminton_court.service.cluster_and_court.BadmintonCourtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminCourtController extends BaseAdminController {
    private final BadmintonCourtService courtService;

    @GetMapping("/courts")
    public ResponseEntity<ApiResponse<Page<BadmintonCourtResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findAll(pageable), "Get courts successfully!!"));
    }

    @GetMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<ApiResponse<Page<BadmintonCourtResponse>>> getByCluster(
            @PathVariable Long clusterId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findByCluster(clusterId, pageable), "Get courts successfully!!"));
    }

    @GetMapping("/courts/{id}")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findById(id), "Get court successfully!!"));
    }

    @PostMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> create(
            @PathVariable Long clusterId,
            @Valid @RequestBody BadmintonCourtRequest request) {
        BadmintonCourtResponse result = courtService.create(clusterId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Create court successfully!!"));
    }

    @PutMapping("/courts/{id}")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BadmintonCourtRequest request) {
        return ResponseEntity.ok(ApiResponse.success(courtService.update(id, request), "Update court successfully!!"));
    }

    @PatchMapping("/courts/{id}/availability")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody CourtAvailabilityRequest request) {
        return ResponseEntity.ok(ApiResponse.success(courtService.updateAvailability(id, request.getAvailable()), "Update availability successfully!!"));
    }

    @DeleteMapping("/courts/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        courtService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete court successfully!!"));
    }
}
