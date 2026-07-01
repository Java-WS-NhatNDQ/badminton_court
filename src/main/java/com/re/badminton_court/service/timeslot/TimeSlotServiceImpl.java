package com.re.badminton_court.service.timeslot;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.timeslot.TimeSlotRequest;
import com.re.badminton_court.model.dto.timeslot.TimeSlotResponse;
import com.re.badminton_court.model.entity.TimeSlot;
import com.re.badminton_court.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TimeSlotServiceImpl implements TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotResponse> findActive() {
        return timeSlotRepository.findByIsActiveTrueOrderByStartTimeAsc().stream()
                .map(TimeSlotServiceImpl::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSlotResponse> findAll() {
        return timeSlotRepository.findAll().stream()
                .sorted(Comparator.comparing(TimeSlot::getStartTime))
                .map(TimeSlotServiceImpl::toResponse)
                .toList();
    }

    @Override
    public TimeSlotResponse create(TimeSlotRequest request) {
        TimeSlot timeSlot = TimeSlot.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .label(resolveLabel(request))
                .basePrice(request.getBasePrice())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        validateTimeRange(timeSlot);
        validateUniqueTimeSlot(timeSlot.getLabel(), timeSlot.getStartTime(), null);
        return toResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public TimeSlotResponse update(Long id, TimeSlotRequest request) {
        TimeSlot timeSlot = findTimeSlot(id);
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setLabel(resolveLabel(request));
        timeSlot.setBasePrice(request.getBasePrice());
        if (request.getIsActive() != null) {
            timeSlot.setIsActive(request.getIsActive());
        }
        validateTimeRange(timeSlot);
        validateUniqueTimeSlot(timeSlot.getLabel(), timeSlot.getStartTime(), id);
        return toResponse(timeSlotRepository.save(timeSlot));
    }

    @Override
    public void delete(Long id) {
        if (!timeSlotRepository.existsById(id)) {
            throw new ResourceNotFoundException("TimeSlot", "id", id);
        }
        timeSlotRepository.deleteById(id);
    }

    private TimeSlot findTimeSlot(Long id) {
        return timeSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot", "id", id));
    }

    private void validateTimeRange(TimeSlot timeSlot) {
        if (!timeSlot.getStartTime().isBefore(timeSlot.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
    }

    private void validateUniqueTimeSlot(String label, LocalTime startTime, Long currentId) {
        boolean duplicatedLabel = currentId == null
                ? timeSlotRepository.existsByLabelIgnoreCase(label)
                : timeSlotRepository.existsByLabelIgnoreCaseAndIdNot(label, currentId);
        if (duplicatedLabel) {
            throw new IllegalArgumentException("Time slot label already exists");
        }

        boolean duplicatedStartTime = currentId == null
                ? timeSlotRepository.existsByStartTime(startTime)
                : timeSlotRepository.existsByStartTimeAndIdNot(startTime, currentId);
        if (duplicatedStartTime) {
            throw new IllegalArgumentException("Time slot start time already exists");
        }
    }

    private String resolveLabel(TimeSlotRequest request) {
        if (request.getLabel() != null && !request.getLabel().isBlank()) {
            return request.getLabel();
        }
        return request.getStartTime() + " - " + request.getEndTime();
    }

    public static TimeSlotResponse toResponse(TimeSlot timeSlot) {
        return new TimeSlotResponse(
                timeSlot.getId(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getLabel(),
                timeSlot.getIsActive(),
                timeSlot.getBasePrice()
        );
    }
}
