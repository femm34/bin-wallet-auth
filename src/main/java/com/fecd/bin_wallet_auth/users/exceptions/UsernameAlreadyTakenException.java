package com.fecd.bin_wallet_auth.users.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsernameAlreadyTakenException extends RuntimeException {

    private final HttpStatus status;

    public UsernameAlreadyTakenException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }
}
