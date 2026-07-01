package com.re.badminton_court.aspect;

import com.re.badminton_court.model.dto.booking.BookingRequest;
import com.re.badminton_court.model.dto.booking.BookingResponse;
import com.re.badminton_court.security.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @AfterReturning(
            pointcut = "execution(* com.re.badminton_court.service.booking.BookingService.create(..))",
            returning = "result"
    )
    public void logBookingSuccess(JoinPoint joinPoint, Object result) {
        if (result instanceof BookingResponse bookingResponse) {
            log.info("[AUDIT - SUCCESS] Customer {} booked court {} on {}, time slot {}",
                    currentUsername(),
                    bookingResponse.courtName(),
                    bookingResponse.bookingDate(),
                    bookingResponse.timeSlotLabel());
        }
    }

    @AfterThrowing(
            pointcut = "execution(* com.re.badminton_court.service.booking.BookingService.create(..))",
            throwing = "exception"
    )
    public void logBookingFailed(JoinPoint joinPoint, Exception exception) {
        BookingRequest request = extractBookingRequest(joinPoint);
        if (request == null) {
            log.warn("[AUDIT - FAILED] Customer {} failed to create booking: {}",
                    currentUsername(), exception.getMessage());
            return;
        }
        log.warn("[AUDIT - FAILED] Customer {} failed to book court {} on {}, time slot {}: {}",
                currentUsername(),
                request.getCourtId(),
                request.getBookingDate(),
                request.getTimeSlotId(),
                exception.getMessage());
    }

    private BookingRequest extractBookingRequest(JoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof BookingRequest bookingRequest) {
                return bookingRequest;
            }
        }
        return null;
    }

    private String currentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "anonymous";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUsername();
        }
        return authentication.getName();
    }
}
