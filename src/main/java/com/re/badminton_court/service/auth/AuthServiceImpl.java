package com.re.badminton_court.service.auth;

import com.re.badminton_court.model.dto.auth.AuthResponse;
import com.re.badminton_court.model.dto.auth.LoginRequest;
import com.re.badminton_court.model.dto.auth.RefreshTokenRequest;
import com.re.badminton_court.model.dto.auth.RegisterRequest;
import com.re.badminton_court.model.entity.RefreshToken;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.model.enums.Role;
import com.re.badminton_court.repository.RefreshTokenRepository;
import com.re.badminton_court.repository.UserRepository;
import com.re.badminton_court.security.CustomUserDetails;
import com.re.badminton_court.security.jwt.JwtProperties;
import com.re.badminton_court.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CUSTOMER).enabled(true)
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

        RefreshToken savedRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryTime(LocalDateTime.now().plusSeconds(
                        jwtProperties.getRefreshTokenExpirationMs() / 1000
                ))
                .build();

        refreshTokenRepository.save(savedRefreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedRefreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (storedRefreshToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(storedRefreshToken);
            throw new JwtException("Refresh token expired");
        }

        User user = storedRefreshToken.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(storedRefreshToken.getToken())
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void logout(String accessToken) {
        tokenBlacklistService.blacklist(accessToken);
    }
}
