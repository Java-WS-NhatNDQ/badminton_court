package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.repository.BadmintonCourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadmintonCourtServiceImpl {
    private final BadmintonCourtRepository courtService;
}
