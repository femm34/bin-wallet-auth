package com.fecd.bin_wallet_auth.shared.utils;

import jakarta.persistence.Column;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtil {

    public void createCookie(HttpServletResponse response, String name, String value, boolean secure, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setValue(value);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(1);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getTokenFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("access_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalStateException("Access token not found!"));
    }
}
