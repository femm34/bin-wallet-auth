package com.fecd.bin_wallet_auth.shared.components;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JwtAuthDetails;
import com.fecd.bin_wallet_auth.authentication.application.service.IJWTService;
import com.fecd.bin_wallet_auth.shared.constants.dts.TokenType;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class OPRJWTFilter extends OncePerRequestFilter {
    private final IJWTService ijwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = ijwtService.extractTokenFromRequest(request);

        Optional.ofNullable(ijwtService.getUsernameFromToken(jwtToken))
                .flatMap(userRepository::findByUsername)
                .ifPresent(user -> settingConfiguration(user, request, jwtToken));
        filterChain.doFilter(request, response);
    }

    private void settingConfiguration(User user, HttpServletRequest request, String jwtToken) {
        request.setAttribute("X-User-Id", user.getId());
        processAuthentication(request, user.toUserDetails(), jwtToken);
    }

    private void processAuthentication(HttpServletRequest request, UserDetails userDetails, String token) {
        Optional.of(token)
                .filter(jwtToken -> !ijwtService.isExpired(jwtToken))
                .filter(jwtToken -> TokenType.ACCESS_TOKEN.name().equals(ijwtService.getClaims(jwtToken).get("type")))
                .ifPresent(jwtToken -> {
                    settingContext(request, userDetails, jwtToken);
                });
    }

    private void settingContext(HttpServletRequest request, UserDetails userDetails, String token) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                        userDetails.getAuthorities());

        JwtAuthDetails authDetails =
                JwtAuthDetails.builder().jwtToken(token).webAuthenticationDetailsSource(new WebAuthenticationDetailsSource().buildDetails(request)).build();
        usernamePasswordAuthenticationToken.setDetails(authDetails);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
