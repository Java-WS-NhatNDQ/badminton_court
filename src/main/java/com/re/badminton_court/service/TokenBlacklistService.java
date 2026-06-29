package com.re.badminton_court.service;

public interface TokenBlacklistService {
    boolean isBlacklisted(String token);
    void blacklistToken(String token);
}
