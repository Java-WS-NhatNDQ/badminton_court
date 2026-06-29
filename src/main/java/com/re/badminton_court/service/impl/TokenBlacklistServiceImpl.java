package com.re.badminton_court.service.impl;

import com.re.badminton_court.model.entity.TokenBlacklist;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.repository.TokenBlacklistRepository;
import com.re.badminton_court.repository.UserRepository;
import com.re.badminton_court.security.jwt.JwtTokenProvider;
import com.re.badminton_court.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.existsByToken(token);
    }

    @Override
    public void blacklistToken(String token) {
        String username = jwtTokenProvider.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElse(null);

        TokenBlacklist blacklistedToken = TokenBlacklist.builder()
                .token(token)
                .expiryTime(jwtTokenProvider.extractExpirationAsLocalDateTime(token))
                .user(user)
                .build();

        tokenBlacklistRepository.save(blacklistedToken);
    }
}
