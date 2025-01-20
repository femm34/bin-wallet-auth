package com.fecd.bin_wallet_auth.shared.utils;

import com.fecd.bin_wallet_auth.authorization.domain.model.PasswordResetToken;
import com.fecd.bin_wallet_auth.authorization.exeptions.PasswordResetTokenExpiredException;
import com.fecd.bin_wallet_auth.authorization.repository.PasswordResetTokenRepository;
import com.fecd.bin_wallet_auth.shared.constants.dts.TokenStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@Component
public class TokenUtils {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public boolean isTokenExpired(String token, String username) {
        PasswordResetToken passwordToken = this.passwordResetTokenRepository.findTokenByUserUsername(username);

        if(passwordToken == null){
            throw new PasswordResetTokenExpiredException("The toke is already expired!");
        }

        long currentMillis = System.currentTimeMillis();
        LocalDateTime currentDate = Instant.ofEpochMilli(currentMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if (currentDate.isAfter(passwordToken.getExpireDate()) && !passwordToken.getStatus().equals("EXPIRED")) {
            this.passwordResetTokenRepository.updateTokenStatus(token, TokenStatus.EXPIRED.name());
            return false;
        }
        return true;
    }

    public boolean isValidToken(String token, String username) {
        return isTokenFound(token) && !isTokenExpired(token, username);
    }

    private boolean isTokenFound(String resetToken) {
        return this.passwordResetTokenRepository.isTokenFound(resetToken);
    }
}
