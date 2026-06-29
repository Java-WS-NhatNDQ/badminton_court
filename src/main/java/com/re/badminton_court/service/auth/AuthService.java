package com.re.badminton_court.service.auth;

import com.re.badminton_court.model.dto.auth.AuthResponse;
import com.re.badminton_court.model.dto.auth.LoginRequest;
import com.re.badminton_court.model.dto.auth.RefreshTokenRequest;
import com.re.badminton_court.model.dto.auth.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
    void logout(String accessToken);
}
