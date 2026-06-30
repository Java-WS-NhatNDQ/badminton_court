package com.re.badminton_court.model.dto.badminton_cluster;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BadmintonClusterRequest {
    @NotBlank
    private String name;

    private String address;
    private String hotLine;
    private Long managerId;
}
