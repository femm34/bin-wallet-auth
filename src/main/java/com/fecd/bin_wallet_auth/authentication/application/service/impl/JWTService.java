package com.fecd.bin_wallet_auth.authentication.application.service.impl;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.authentication.application.service.IJWTService;
import com.fecd.bin_wallet_auth.authorization.domain.mapper.RoleMapper;
import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@Service
public class JWTService implements IJWTService {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.issuer}")
    private String jwtIssuer;


    @Override
    public JWTResponse generateToken(Long userId, long expiryTime, String type, String username, String name,
                                     Set<Role> authorities) {
        return JWTResponse.builder().token(Jwts.builder()
                .setIssuer(jwtIssuer)
                .setSubject(String.valueOf(userId))
                .setExpiration(new Date(System.currentTimeMillis() + expiryTime))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .claim("type", type)
                .claim("username", username)
                .claim("name", name)
                .claim("authorities", RoleMapper.toRolesString(authorities))
                .signWith(getSigningKey())
                .compact()).build();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean isAValidToken(String token) {
        return false;
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return false;
    }

    @Override
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isExpired(String token) {
        try {
            return this.getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            return Long.parseLong(getClaims(token).getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            return getClaims(token).get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "access_token".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
