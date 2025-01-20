package com.fecd.bin_wallet_auth.users.service;

import com.fecd.bin_wallet_auth.shared.utils.TokenUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordResetService {

    private final TokenUtils tokenUtils;

    public boolean validateResetPasswordToken(String token, String username) {
        return tokenUtils.isValidToken(token, username);
    }

}
