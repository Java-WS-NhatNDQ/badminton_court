package com.re.badminton_court.repository;

import com.re.badminton_court.model.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByIsActiveTrueOrderByStartTimeAsc();
    boolean existsByLabelIgnoreCase(String label);
    boolean existsByLabelIgnoreCaseAndIdNot(String label, Long id);
    boolean existsByStartTime(LocalTime startTime);
    boolean existsByStartTimeAndIdNot(LocalTime startTime, Long id);
}
