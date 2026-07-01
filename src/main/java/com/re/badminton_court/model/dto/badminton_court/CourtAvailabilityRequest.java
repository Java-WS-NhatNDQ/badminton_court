package com.re.badminton_court.model.dto.badminton_court;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourtAvailabilityRequest {
    @NotNull
    private Boolean available;
}
