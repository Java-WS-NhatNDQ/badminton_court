package com.re.badminton_court.service.booking;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.booking.BookingRequest;
import com.re.badminton_court.model.dto.booking.BookingResponse;
import com.re.badminton_court.model.entity.BadmintonCluster;
import com.re.badminton_court.model.entity.BadmintonCourt;
import com.re.badminton_court.model.entity.Booking;
import com.re.badminton_court.model.entity.TimeSlot;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.model.enums.BookingStatus;
import com.re.badminton_court.repository.BadmintonCourtRepository;
import com.re.badminton_court.repository.BookingRepository;
import com.re.badminton_court.repository.TimeSlotRepository;
import com.re.badminton_court.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private static final List<BookingStatus> BUSY_STATUSES = List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final BookingRepository bookingRepository;
    private final BadmintonCourtRepository courtRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    public BookingResponse create(BookingRequest request) {
        User user = currentUser();
        BadmintonCourt court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonCourt", "id", request.getCourtId()));
        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("TimeSlot", "id", request.getTimeSlotId()));

        if (!Boolean.TRUE.equals(court.getIsAvailable())) {
            throw new IllegalArgumentException("Court is not available");
        }
        if (!Boolean.TRUE.equals(timeSlot.getIsActive())) {
            throw new IllegalArgumentException("Time slot is inactive");
        }
        if (isBusy(court.getId(), request)) {
            throw new IllegalArgumentException("Court is already booked for this date and time slot");
        }

        Booking booking = Booking.builder()
                .bookingDate(request.getBookingDate())
                .timeSlot(timeSlot)
                .totalPrice(timeSlot.getBasePrice())
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .user(user)
                .badmintonCourt(court)
                .build();

        return toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> findMyBookings(Pageable pageable) {
        return bookingRepository.findByUserId(currentUser().getId(), pageable).map(BookingServiceImpl::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse findMyBookingById(Long id) {
        return toResponse(findMyBooking(id));
    }

    @Override
    public BookingResponse cancelMyBooking(Long id) {
        Booking booking = findMyBooking(id);
        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Checked-in booking cannot be cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(BookingServiceImpl::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> findManagerBookings(Pageable pageable) {
        return bookingRepository.findByBadmintonCourtClusterManagerId(currentUser().getId(), pageable)
                .map(BookingServiceImpl::toResponse);
    }

    @Override
    public BookingResponse confirmByManager(Long id) {
        return updateManagerBookingStatus(id, BookingStatus.CONFIRMED);
    }

    @Override
    public BookingResponse cancelByManager(Long id) {
        return updateManagerBookingStatus(id, BookingStatus.CANCELLED);
    }

    @Override
    public BookingResponse confirmByAdmin(Long id) {
        return updateAdminBookingStatus(id, BookingStatus.CONFIRMED);
    }

    @Override
    public BookingResponse cancelByAdmin(Long id) {
        return updateAdminBookingStatus(id, BookingStatus.CANCELLED);
    }

    @Override
    public BookingResponse checkInByManager(Long id) {
        Booking booking = findManagerBooking(id);
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed booking can be checked in");
        }
        booking.setStatus(BookingStatus.CHECKED_IN);
        return toResponse(bookingRepository.save(booking));
    }

    private BookingResponse updateManagerBookingStatus(Long id, BookingStatus status) {
        Booking booking = findManagerBooking(id);
        validateStatusChange(booking, status);
        booking.setStatus(status);
        return toResponse(bookingRepository.save(booking));
    }

    private BookingResponse updateAdminBookingStatus(Long id, BookingStatus status) {
        Booking booking = findBooking(id);
        validateStatusChange(booking, status);
        booking.setStatus(status);
        return toResponse(bookingRepository.save(booking));
    }

    private void validateStatusChange(Booking booking, BookingStatus status) {
        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Checked-in booking cannot be changed");
        }
        if (status == BookingStatus.CONFIRMED && booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled booking cannot be confirmed");
        }
    }

    private boolean isBusy(Long courtId, BookingRequest request) {
        return bookingRepository.existsByBadmintonCourtIdAndBookingDateAndTimeSlotIdAndStatusIn(
                courtId, request.getBookingDate(), request.getTimeSlotId(), BUSY_STATUSES);
    }

    private Booking findBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
    }

    private Booking findMyBooking(Long id) {
        Booking booking = findBooking(id);
        if (!booking.getUser().getId().equals(currentUser().getId())) {
            throw new IllegalArgumentException("You do not own this booking");
        }
        return booking;
    }

    private Booking findManagerBooking(Long id) {
        Booking booking = findBooking(id);
        BadmintonCluster cluster = booking.getBadmintonCourt().getCluster();
        if (cluster.getManager() == null || !cluster.getManager().getId().equals(currentUser().getId())) {
            throw new IllegalArgumentException("You do not manage this booking");
        }
        return booking;
    }

    private User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        throw new IllegalArgumentException("Authenticated user not found");
    }

    public static BookingResponse toResponse(Booking booking) {
        BadmintonCourt court = booking.getBadmintonCourt();
        BadmintonCluster cluster = court.getCluster();
        TimeSlot timeSlot = booking.getTimeSlot();
        User user = booking.getUser();
        return new BookingResponse(
                booking.getId(),
                booking.getBookingDate(),
                timeSlot.getId(),
                timeSlot.getLabel(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getCreatedAt(),
                user.getId(),
                user.getUsername(),
                court.getId(),
                court.getCourtName(),
                cluster != null ? cluster.getId() : null,
                cluster != null ? cluster.getName() : null
        );
    }
}
