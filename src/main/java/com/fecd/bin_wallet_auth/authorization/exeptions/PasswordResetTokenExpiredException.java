package com.fecd.bin_wallet_auth.authorization.exeptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PasswordResetTokenExpiredException extends RuntimeException {

    private final HttpStatus status;

    public PasswordResetTokenExpiredException(String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;
    }
}
