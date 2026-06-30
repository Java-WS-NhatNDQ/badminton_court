package com.re.badminton_court.repository;

import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterResponse;
import com.re.badminton_court.model.entity.BadmintonCluster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadmintonClusterRepository extends JpaRepository<BadmintonCluster, Long> {
    Page<BadmintonClusterResponse> findByManagerId(Long managerId, Pageable pageable);
    boolean existsByNameAndAddress(String name, String address);
}
