package com.fecd.bin_wallet_auth.users.application.service;

import com.fecd.bin_wallet_auth.authentication.application.service.impl.JWTService;
import com.fecd.bin_wallet_auth.shared.constants.dts.JWTProperties;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.shared.utils.CookieUtil;
import com.fecd.bin_wallet_auth.users.application.dtos.UserBasics;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final CookieUtil cookieUtil;

    @Override
    public void deleteUserById(HttpServletRequest request, HttpServletResponse response) {
        String token = cookieUtil.getTokenFromCookie(request);
        String userId = this.jwtService.getUserIdFromToken(token);
        String username = this.jwtService.getUsernameFromToken(token);
        this.userRepository.deleteById(Long.parseLong(userId));
        this.cookieUtil.clearCookie(response, "access_token");
    }

    @Override
    public BinWalletResponse updateUserById(HttpServletRequest request) {

        return null;
    }

    @Override
    public UserBasics getUserById(HttpServletRequest request) {
        return null;
    }

    @Override
    public List<UserBasics> getAllUsers() {
        return List.of();
    }
}
