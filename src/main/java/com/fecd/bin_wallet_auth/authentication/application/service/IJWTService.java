package com.fecd.bin_wallet_auth.authentication.application.service;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public interface IJWTService {
    JWTResponse generateToken(Long userId, long timeMillis, String type, String username, String name, Set<Role> authorities);
    boolean isAValidToken(String token);
    boolean validateRefreshToken(String token);
    Claims getClaims(String token);
    boolean isExpired(String token);
    Long getUserIdFromToken(String token);
    String getUsernameFromToken(String token);
    String extractTokenFromRequest(HttpServletRequest request);
}
