package com.re.badminton_court.service.auth;

public interface TokenBlacklistService {
    boolean isBlacklisted(String token);
    void blacklist(String token);
}
