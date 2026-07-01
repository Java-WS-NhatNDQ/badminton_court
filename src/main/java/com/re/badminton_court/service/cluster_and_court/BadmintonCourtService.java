package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtRequest;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface BadmintonCourtService {
    Page<BadmintonCourtResponse> findAll(Pageable pageable);
    BadmintonCourtResponse findById(Long id);
    Page<BadmintonCourtResponse> findByCluster(Long clusterId, Pageable pageable);
    BadmintonCourtResponse create(Long clusterId, BadmintonCourtRequest request);
    BadmintonCourtResponse update(Long id, BadmintonCourtRequest request);
    BadmintonCourtResponse updateAvailability(Long id, Boolean available);
    void delete(Long id);
    Page<BadmintonCourtResponse> findMyCourtsByCluster(Long clusterId, Pageable pageable);
    BadmintonCourtResponse findMyCourtById(Long id);
    BadmintonCourtResponse createMyCourt(Long clusterId, BadmintonCourtRequest request);
    BadmintonCourtResponse updateMyCourt(Long id, BadmintonCourtRequest request);
    BadmintonCourtResponse updateMyCourtAvailability(Long id, Boolean available);
    void deleteMyCourt(Long id);
    List<BadmintonCourtResponse> findAvailableCourts(Long clusterId, LocalDate date, Long timeSlotId);
}
