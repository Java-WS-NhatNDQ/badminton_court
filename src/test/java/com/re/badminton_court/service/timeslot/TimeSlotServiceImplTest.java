package com.re.badminton_court.service.timeslot;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.timeslot.TimeSlotRequest;
import com.re.badminton_court.model.dto.timeslot.TimeSlotResponse;
import com.re.badminton_court.model.entity.TimeSlot;
import com.re.badminton_court.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceImplTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @InjectMocks
    private TimeSlotServiceImpl timeSlotService;

    @Test
    void findActive_returnsActiveTimeSlots() {
        TimeSlot morning = timeSlot(1L, "Morning", LocalTime.of(8, 0), LocalTime.of(9, 0), true);
        when(timeSlotRepository.findByIsActiveTrueOrderByStartTimeAsc()).thenReturn(List.of(morning));

        List<TimeSlotResponse> result = timeSlotService.findActive();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Morning", result.get(0).label());
    }

    @Test
    void findAll_sortsByStartTime() {
        TimeSlot late = timeSlot(2L, "Late", LocalTime.of(10, 0), LocalTime.of(11, 0), true);
        TimeSlot early = timeSlot(1L, "Early", LocalTime.of(7, 0), LocalTime.of(8, 0), true);
        when(timeSlotRepository.findAll()).thenReturn(List.of(late, early));

        List<TimeSlotResponse> result = timeSlotService.findAll();

        assertEquals(List.of(1L, 2L), result.stream().map(TimeSlotResponse::id).toList());
    }

    @Test
    void create_defaultsActiveAndGeneratedLabelWhenMissing() {
        TimeSlotRequest request = request(LocalTime.of(8, 0), LocalTime.of(9, 0), null, 100000D, null);
        when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(invocation -> {
            TimeSlot saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        TimeSlotResponse result = timeSlotService.create(request);

        ArgumentCaptor<TimeSlot> captor = ArgumentCaptor.forClass(TimeSlot.class);
        verify(timeSlotRepository).save(captor.capture());
        assertEquals("08:00 - 09:00", captor.getValue().getLabel());
        assertTrue(captor.getValue().getIsActive());
        assertEquals(10L, result.id());
    }

    @Test
    void create_throwsWhenStartTimeIsNotBeforeEndTime() {
        TimeSlotRequest request = request(LocalTime.of(9, 0), LocalTime.of(9, 0), "Invalid", 100000D, true);

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.create(request));
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    @Test
    void create_throwsWhenLabelAlreadyExists() {
        TimeSlotRequest request = request(LocalTime.of(8, 0), LocalTime.of(9, 0), "Morning", 100000D, true);
        when(timeSlotRepository.existsByLabelIgnoreCase("Morning")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.create(request));
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    @Test
    void create_throwsWhenStartTimeAlreadyExists() {
        TimeSlotRequest request = request(LocalTime.of(8, 0), LocalTime.of(9, 0), "Morning", 100000D, true);
        when(timeSlotRepository.existsByStartTime(LocalTime.of(8, 0))).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> timeSlotService.create(request));
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    @Test
    void delete_throwsWhenTimeSlotNotFound() {
        when(timeSlotRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> timeSlotService.delete(99L));
        verify(timeSlotRepository, never()).deleteById(99L);
    }

    private TimeSlot timeSlot(Long id, String label, LocalTime startTime, LocalTime endTime, Boolean active) {
        return TimeSlot.builder()
                .id(id)
                .label(label)
                .startTime(startTime)
                .endTime(endTime)
                .isActive(active)
                .basePrice(100000D)
                .build();
    }

    private TimeSlotRequest request(LocalTime startTime, LocalTime endTime, String label, Double price, Boolean active) {
        TimeSlotRequest request = new TimeSlotRequest();
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setLabel(label);
        request.setBasePrice(price);
        request.setIsActive(active);
        return request;
    }
}
