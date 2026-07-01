package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.repository.BadmintonClusterRepository;
import com.re.badminton_court.repository.BadmintonCourtRepository;
import com.re.badminton_court.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadmintonCourtServiceImplTest {

    @Mock
    private BadmintonCourtRepository courtRepository;

    @Mock
    private BadmintonClusterRepository clusterRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BadmintonCourtServiceImpl courtService;

    @Test
    void findByCluster_throwsWhenClusterNotFound() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(clusterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courtService.findByCluster(99L, pageable));
        verify(courtRepository, never()).findByClusterId(99L, pageable);
    }
}
