package com.re.badminton_court.controller.manager;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManagerClusterController extends BaseManagerController {
    private final BadmintonClusterService clusterService;

    @GetMapping("/clusters")
    public ResponseEntity<ApiResponse<Page<BadmintonClusterResponse>>> getAllMyClusters(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.findMyClusters(pageable), "Get my clusters successfully!!"));
    }

    @GetMapping("/clusters/{id}")
    public ResponseEntity<ApiResponse<BadmintonClusterResponse>> getMyClusterDetails(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.findMyClusterById(id), "Get my cluster successfully!!"));
    }

    @PutMapping("/clusters/{id}")
    public ResponseEntity<ApiResponse<BadmintonClusterResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BadmintonClusterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(clusterService.updateMyCluster(id, request), "Update my cluster successfully!!"));
    }
}
