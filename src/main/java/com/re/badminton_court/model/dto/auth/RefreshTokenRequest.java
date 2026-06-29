package com.re.badminton_court.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RefreshTokenRequest {
    @NotBlank
    private String refreshToken;
}
