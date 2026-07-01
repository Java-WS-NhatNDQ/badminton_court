package com.re.badminton_court.controller.admin;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterRequest;
import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterResponse;
import com.re.badminton_court.service.cluster_and_court.BadmintonClusterService;
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
public class AdminClusterController extends BaseAdminController {
    private final BadmintonClusterService clusterService;

    @GetMapping("/clusters")
    public ResponseEntity<ApiResponse<Page<BadmintonClusterResponse>>> getAll(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.findAll(pageable), "Get clusters successfully!!"));
    }

    @GetMapping("/clusters/{id}")
    public ResponseEntity<ApiResponse<BadmintonClusterResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.findById(id), "Get cluster successfully!!"));
    }

    @PostMapping("/clusters")
    public ResponseEntity<ApiResponse<BadmintonClusterResponse>> create(@Valid @RequestBody BadmintonClusterRequest request) {
        BadmintonClusterResponse result = clusterService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Create cluster successfully!!"));
    }

    @PutMapping("/clusters/{id}")
    public ResponseEntity<ApiResponse<BadmintonClusterResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BadmintonClusterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.update(id, request), "Update cluster successfully!!"));
    }

    @PatchMapping("/clusters/{id}/manager/{managerId}")
    public ResponseEntity<ApiResponse<BadmintonClusterResponse>> assignManager(
            @PathVariable Long id,
            @PathVariable Long managerId) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.assignManager(id, managerId), "Assign manager successfully!!"));
    }

    @DeleteMapping("/clusters/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        clusterService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete cluster successfully!!"));
    }
}
