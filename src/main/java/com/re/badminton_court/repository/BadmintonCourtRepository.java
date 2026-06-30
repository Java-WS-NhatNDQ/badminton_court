package com.re.badminton_court.repository;

import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtResponse;
import com.re.badminton_court.model.entity.BadmintonCourt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadmintonCourtRepository extends JpaRepository<BadmintonCourt, Long> {
    Page<BadmintonCourtResponse> findByClusterId(Long clusterId, Pageable pageable);
    Page<BadmintonCourtResponse> findByClusterManagerId(Long managerId, Pageable pageable);
    boolean existsByCourtNameAndClusterId(String courtName, Long clusterId);
    List<BadmintonCourt> findByClusterIdAndIsAvailableTrue(Long clusterId);
}
