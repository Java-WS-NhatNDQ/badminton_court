package com.re.badminton_court.model.dto.court_image;

public record CourtImageResponse(
        Long id,
        String imageUrl,
        Boolean isMain,
        Long courtId
) {}
