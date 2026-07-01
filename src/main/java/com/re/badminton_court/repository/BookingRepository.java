package com.re.badminton_court.repository;

import com.re.badminton_court.model.entity.Booking;
import com.re.badminton_court.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByBadmintonCourtIdAndBookingDateAndTimeSlotIdAndStatusIn(
            Long courtId,
            LocalDate bookingDate,
            Long timeSlotId,
            Collection<BookingStatus> statuses
    );

    Page<Booking> findByUserId(Long userId, Pageable pageable);
    Page<Booking> findByBadmintonCourtClusterManagerId(Long managerId, Pageable pageable);
    Page<Booking> findByBadmintonCourtClusterId(Long clusterId, Pageable pageable);
}
