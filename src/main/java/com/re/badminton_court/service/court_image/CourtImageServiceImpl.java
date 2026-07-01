package com.re.badminton_court.service.court_image;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.cloudinary.ImageResponse;
import com.re.badminton_court.model.dto.court_image.CourtImageResponse;
import com.re.badminton_court.model.entity.BadmintonCluster;
import com.re.badminton_court.model.entity.BadmintonCourt;
import com.re.badminton_court.model.entity.CourtImage;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.repository.BadmintonCourtRepository;
import com.re.badminton_court.repository.CourtImageRepository;
import com.re.badminton_court.security.CustomUserDetails;
import com.re.badminton_court.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourtImageServiceImpl implements CourtImageService {
    private final CourtImageRepository courtImageRepository;
    private final BadmintonCourtRepository courtRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<CourtImageResponse> findByCourt(Long courtId) {
        return courtImageRepository.findByBadmintonCourtId(courtId).stream()
                .map(CourtImageServiceImpl::toResponse)
                .toList();
    }

    @Override
    public CourtImageResponse uploadMyCourtImage(Long courtId, MultipartFile file, Boolean main) {
        BadmintonCourt court = findMyCourt(courtId);
        ImageResponse image = cloudinaryService.uploadImage(file);

        boolean isMain = Boolean.TRUE.equals(main);
        if (isMain) {
            courtImageRepository.findByBadmintonCourtId(courtId)
                    .forEach(existing -> existing.setIsMain(false));
            court.setImageUrl(image.getUrl());
            courtRepository.save(court);
        }

        CourtImage courtImage = CourtImage.builder()
                .badmintonCourt(court)
                .imageUrl(image.getUrl())
                .isMain(isMain)
                .build();
        return toResponse(courtImageRepository.save(courtImage));
    }

    @Override
    public void deleteMyCourtImage(Long imageId) {
        CourtImage image = courtImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("CourtImage", "id", imageId));
        findMyCourt(image.getBadmintonCourt().getId());
        courtImageRepository.delete(image);
    }

    private BadmintonCourt findMyCourt(Long courtId) {
        BadmintonCourt court = courtRepository.findById(courtId)
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonCourt", "id", courtId));
        User user = currentUser();
        BadmintonCluster cluster = court.getCluster();
        if (cluster.getManager() == null || !cluster.getManager().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not manage this court");
        }
        return court;
    }

    private User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        throw new IllegalArgumentException("Authenticated user not found");
    }

    private static CourtImageResponse toResponse(CourtImage image) {
        return new CourtImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getIsMain(),
                image.getBadmintonCourt().getId()
        );
    }
}
