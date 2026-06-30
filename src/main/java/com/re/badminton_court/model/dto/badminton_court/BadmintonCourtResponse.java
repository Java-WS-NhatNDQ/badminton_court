package com.re.badminton_court.model.dto.badminton_court;

public record BadmintonCourtResponse(
        Long id,
        String courtName,
        String type,
        String imageUrl,
        Boolean isAvailable,
        Long clusterId,
        String clusterName
) {}
