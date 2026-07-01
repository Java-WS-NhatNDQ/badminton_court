package com.re.badminton_court.model.dto.badminton_court;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BadmintonCourtRequest {
    @NotBlank
    private String courtName;

    @NotBlank
    private String type;

    private String imageUrl;
    private Boolean isAvailable;
}
