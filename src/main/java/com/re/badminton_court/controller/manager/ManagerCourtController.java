package com.re.badminton_court.controller.manager;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtRequest;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtResponse;
import com.re.badminton_court.model.dto.badminton_court.CourtAvailabilityRequest;
import com.re.badminton_court.model.dto.court_image.CourtImageResponse;
import com.re.badminton_court.service.cluster_and_court.BadmintonCourtService;
import com.re.badminton_court.service.court_image.CourtImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManagerCourtController extends BaseManagerController {
    private final BadmintonCourtService courtService;
    private final CourtImageService courtImageService;

    @GetMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<ApiResponse<Page<BadmintonCourtResponse>>> getMyCourtsByCluster(
            @PathVariable Long clusterId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findMyCourtsByCluster(clusterId, pageable), "Get my courts successfully!!"));
    }

    @PostMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> create(
            @PathVariable Long clusterId,
            @Valid @RequestBody BadmintonCourtRequest request) {
        BadmintonCourtResponse result = courtService.createMyCourt(clusterId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Create court successfully!!"));
    }

    @GetMapping("/courts/{courtId}")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> getById(@PathVariable Long courtId) {
        return ResponseEntity.ok(ApiResponse.success(courtService.findMyCourtById(courtId), "Get court successfully!!"));
    }

    @PutMapping("/courts/{courtId}")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> update(
            @PathVariable Long courtId,
            @Valid @RequestBody BadmintonCourtRequest request) {
        return ResponseEntity.ok(ApiResponse.success(courtService.updateMyCourt(courtId, request), "Update court successfully!!"));
    }

    @PatchMapping("/courts/{courtId}/availability")
    public ResponseEntity<ApiResponse<BadmintonCourtResponse>> updateAvailability(
            @PathVariable Long courtId,
            @Valid @RequestBody CourtAvailabilityRequest request) {
        return ResponseEntity.ok(ApiResponse.success(courtService.updateMyCourtAvailability(courtId, request.getAvailable()), "Update availability successfully!!"));
    }

    @DeleteMapping("/courts/{courtId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long courtId) {
        courtService.deleteMyCourt(courtId);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete court successfully!!"));
    }

    @GetMapping("/courts/{courtId}/images")
    public ResponseEntity<ApiResponse<List<CourtImageResponse>>> getImages(@PathVariable Long courtId) {
        courtService.findMyCourtById(courtId);
        return ResponseEntity.ok(ApiResponse.success(courtImageService.findByCourt(courtId), "Get court images successfully!!"));
    }

    @PostMapping("/courts/{courtId}/images")
    public ResponseEntity<ApiResponse<CourtImageResponse>> uploadImage(
            @PathVariable Long courtId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "main", required = false) Boolean main) {
        CourtImageResponse result = courtImageService.uploadMyCourtImage(courtId, file, main);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Upload court image successfully!!"));
    }

    @DeleteMapping("/court-images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long imageId) {
        courtImageService.deleteMyCourtImage(imageId);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete court image successfully!!"));
    }
}
