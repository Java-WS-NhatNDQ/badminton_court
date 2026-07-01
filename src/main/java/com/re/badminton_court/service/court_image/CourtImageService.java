package com.re.badminton_court.service.court_image;

import com.re.badminton_court.model.dto.court_image.CourtImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourtImageService {
    List<CourtImageResponse> findByCourt(Long courtId);
    CourtImageResponse uploadMyCourtImage(Long courtId, MultipartFile file, Boolean main);
    void deleteMyCourtImage(Long imageId);
}
