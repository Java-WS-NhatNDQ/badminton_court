package com.re.badminton_court.model.dto.badminton_cluster;

public record BadmintonClusterResponse(
        Long id,
        String name,
        String address,
        String hotLine,
        Long managerId,
        String managerName
) {}
