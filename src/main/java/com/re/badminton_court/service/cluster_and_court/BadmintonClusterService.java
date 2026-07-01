package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterRequest;
import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BadmintonClusterService {
    Page<BadmintonClusterResponse> findAll(Pageable pageable);
    BadmintonClusterResponse findById(Long id);
    BadmintonClusterResponse create(BadmintonClusterRequest request);
    BadmintonClusterResponse update(Long id, BadmintonClusterRequest request);
    BadmintonClusterResponse assignManager(Long id, Long managerId);
    void delete(Long id);
    Page<BadmintonClusterResponse> findMyClusters(Pageable pageable);
    BadmintonClusterResponse findMyClusterById(Long id);
    BadmintonClusterResponse updateMyCluster(Long id, BadmintonClusterRequest request);
}
