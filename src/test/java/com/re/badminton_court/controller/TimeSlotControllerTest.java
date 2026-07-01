package com.re.badminton_court.controller;

import com.re.badminton_court.controller.admin.AdminTimeSlotController;
import com.re.badminton_court.controller.open.TimeSlotController;
import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.timeslot.TimeSlotRequest;
import com.re.badminton_court.model.dto.timeslot.TimeSlotResponse;
import com.re.badminton_court.service.timeslot.TimeSlotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeSlotControllerTest {

    @Mock
    private TimeSlotService timeSlotService;

    @Test
    void openGetActive_returnsSuccessResponse() {
        TimeSlotResponse timeSlot = response(1L, "Morning");
        when(timeSlotService.findActive()).thenReturn(List.of(timeSlot));
        TimeSlotController controller = new TimeSlotController(timeSlotService);

        ResponseEntity<ApiResponse<List<TimeSlotResponse>>> response = controller.getActive();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals(List.of(timeSlot), response.getBody().getData());
    }

    @Test
    void adminGetAll_returnsSuccessResponse() {
        TimeSlotResponse timeSlot = response(1L, "Morning");
        when(timeSlotService.findAll()).thenReturn(List.of(timeSlot));
        AdminTimeSlotController controller = new AdminTimeSlotController(timeSlotService);

        ResponseEntity<ApiResponse<List<TimeSlotResponse>>> response = controller.getAll();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals(List.of(timeSlot), response.getBody().getData());
    }

    @Test
    void adminCreate_returnsCreatedResponse() {
        TimeSlotRequest request = request();
        TimeSlotResponse created = response(10L, "Morning");
        when(timeSlotService.create(request)).thenReturn(created);
        AdminTimeSlotController controller = new AdminTimeSlotController(timeSlotService);

        ResponseEntity<ApiResponse<TimeSlotResponse>> response = controller.create(request);

        assertEquals(201, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals(created, response.getBody().getData());
    }

    @Test
    void adminUpdate_returnsUpdatedResponse() {
        TimeSlotRequest request = request();
        TimeSlotResponse updated = response(10L, "Updated");
        when(timeSlotService.update(10L, request)).thenReturn(updated);
        AdminTimeSlotController controller = new AdminTimeSlotController(timeSlotService);

        ResponseEntity<ApiResponse<TimeSlotResponse>> response = controller.update(10L, request);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertEquals(updated, response.getBody().getData());
    }

    @Test
    void adminDelete_returnsSuccessResponseWithNullData() {
        AdminTimeSlotController controller = new AdminTimeSlotController(timeSlotService);

        ResponseEntity<ApiResponse<Void>> response = controller.delete(10L);

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
        verify(timeSlotService).delete(10L);
    }

    private TimeSlotResponse response(Long id, String label) {
        return new TimeSlotResponse(
                id,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                label,
                true,
                100000D
        );
    }

    private TimeSlotRequest request() {
        TimeSlotRequest request = new TimeSlotRequest();
        request.setStartTime(LocalTime.of(8, 0));
        request.setEndTime(LocalTime.of(9, 0));
        request.setLabel("Morning");
        request.setBasePrice(100000D);
        request.setIsActive(true);
        return request;
    }
}
