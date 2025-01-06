package com.fecd.bin_wallet_auth.authentication.application.service.impl;

import com.fecd.bin_wallet_auth.users.service.UserFailedAttemptsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomLoginFailure {

    private final UserFailedAttemptsService userFailedAttemptsService;

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

    }
}
