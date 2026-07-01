package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtRequest;
import com.re.badminton_court.model.dto.badminton_court.BadmintonCourtResponse;
import com.re.badminton_court.model.entity.BadmintonCluster;
import com.re.badminton_court.model.entity.BadmintonCourt;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.model.enums.BookingStatus;
import com.re.badminton_court.repository.BadmintonClusterRepository;
import com.re.badminton_court.repository.BadmintonCourtRepository;
import com.re.badminton_court.repository.BookingRepository;
import com.re.badminton_court.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BadmintonCourtServiceImpl implements BadmintonCourtService {
    private static final List<BookingStatus> BUSY_STATUSES = List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final BadmintonCourtRepository courtRepository;
    private final BadmintonClusterRepository clusterRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BadmintonCourtResponse> findAll(Pageable pageable) {
        return courtRepository.findAll(pageable).map(BadmintonCourtServiceImpl::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BadmintonCourtResponse findById(Long id) {
        return toResponse(findCourt(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BadmintonCourtResponse> findByCluster(Long clusterId, Pageable pageable) {
        findCluster(clusterId);
        return courtRepository.findByClusterId(clusterId, pageable).map(BadmintonCourtServiceImpl::toResponse);
    }

    @Override
    public BadmintonCourtResponse create(Long clusterId, BadmintonCourtRequest request) {
        BadmintonCluster cluster = findCluster(clusterId);
        validateCourtName(request.getCourtName(), clusterId, null);
        BadmintonCourt court = BadmintonCourt.builder()
                .courtName(request.getCourtName())
                .type(request.getType())
                .imageUrl(request.getImageUrl())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .cluster(cluster)
                .build();
        return toResponse(courtRepository.save(court));
    }

    @Override
    public BadmintonCourtResponse update(Long id, BadmintonCourtRequest request) {
        BadmintonCourt court = findCourt(id);
        validateCourtName(request.getCourtName(), court.getCluster().getId(), id);
        updateCourtFields(court, request);
        return toResponse(courtRepository.save(court));
    }

    @Override
    public BadmintonCourtResponse updateAvailability(Long id, Boolean available) {
        BadmintonCourt court = findCourt(id);
        court.setIsAvailable(available);
        return toResponse(courtRepository.save(court));
    }

    @Override
    public void delete(Long id) {
        if (!courtRepository.existsById(id)) {
            throw new ResourceNotFoundException("BadmintonCourt", "id", id);
        }
        courtRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BadmintonCourtResponse> findMyCourtsByCluster(Long clusterId, Pageable pageable) {
        findMyCluster(clusterId);
        return courtRepository.findByClusterId(clusterId, pageable).map(BadmintonCourtServiceImpl::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BadmintonCourtResponse findMyCourtById(Long id) {
        return toResponse(findMyCourt(id));
    }

    @Override
    public BadmintonCourtResponse createMyCourt(Long clusterId, BadmintonCourtRequest request) {
        findMyCluster(clusterId);
        return create(clusterId, request);
    }

    @Override
    public BadmintonCourtResponse updateMyCourt(Long id, BadmintonCourtRequest request) {
        BadmintonCourt court = findMyCourt(id);
        validateCourtName(request.getCourtName(), court.getCluster().getId(), id);
        updateCourtFields(court, request);
        return toResponse(courtRepository.save(court));
    }

    @Override
    public BadmintonCourtResponse updateMyCourtAvailability(Long id, Boolean available) {
        BadmintonCourt court = findMyCourt(id);
        court.setIsAvailable(available);
        return toResponse(courtRepository.save(court));
    }

    @Override
    public void deleteMyCourt(Long id) {
        findMyCourt(id);
        courtRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadmintonCourtResponse> findAvailableCourts(Long clusterId, LocalDate date, Long timeSlotId) {
        List<BadmintonCourt> courts = clusterId != null
                ? courtRepository.findByClusterIdAndIsAvailableTrue(clusterId)
                : courtRepository.findByIsAvailableTrue();

        return courts.stream()
                .filter(court -> isFree(court, date, timeSlotId))
                .map(BadmintonCourtServiceImpl::toResponse)
                .toList();
    }

    private boolean isFree(BadmintonCourt court, LocalDate date, Long timeSlotId) {
        if (date == null || timeSlotId == null) {
            return true;
        }
        return !bookingRepository.existsByBadmintonCourtIdAndBookingDateAndTimeSlotIdAndStatusIn(
                court.getId(), date, timeSlotId, BUSY_STATUSES);
    }

    private void updateCourtFields(BadmintonCourt court, BadmintonCourtRequest request) {
        court.setCourtName(request.getCourtName());
        court.setType(request.getType());
        court.setImageUrl(request.getImageUrl());
        if (request.getIsAvailable() != null) {
            court.setIsAvailable(request.getIsAvailable());
        }
    }

    private void validateCourtName(String courtName, Long clusterId, Long currentCourtId) {
        if (!courtRepository.existsByCourtNameAndClusterId(courtName, clusterId)) {
            return;
        }
        if (currentCourtId == null) {
            throw new IllegalArgumentException("Court name already exists in this cluster");
        }
        BadmintonCourt current = findCourt(currentCourtId);
        if (!current.getCourtName().equals(courtName)) {
            throw new IllegalArgumentException("Court name already exists in this cluster");
        }
    }

    private BadmintonCourt findCourt(Long id) {
        return courtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonCourt", "id", id));
    }

    private BadmintonCluster findCluster(Long id) {
        return clusterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonCluster", "id", id));
    }

    private BadmintonCluster findMyCluster(Long id) {
        BadmintonCluster cluster = findCluster(id);
        User user = currentUser();
        if (cluster.getManager() == null || !cluster.getManager().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not manage this cluster");
        }
        return cluster;
    }

    private BadmintonCourt findMyCourt(Long id) {
        BadmintonCourt court = findCourt(id);
        User user = currentUser();
        BadmintonCluster cluster = court.getCluster();
        if (cluster.getManager() == null || !cluster.getManager().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not manage this court");
        }
        return court;
    }

    private User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        throw new IllegalArgumentException("Authenticated user not found");
    }

    public static BadmintonCourtResponse toResponse(BadmintonCourt court) {
        BadmintonCluster cluster = court.getCluster();
        return new BadmintonCourtResponse(
                court.getId(),
                court.getCourtName(),
                court.getType(),
                court.getImageUrl(),
                court.getIsAvailable(),
                cluster != null ? cluster.getId() : null,
                cluster != null ? cluster.getName() : null
        );
    }
}
