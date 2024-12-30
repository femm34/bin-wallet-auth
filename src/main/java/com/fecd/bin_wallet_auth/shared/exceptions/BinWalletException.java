package com.fecd.bin_wallet_auth.shared.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BinWalletException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final HttpStatusCode httpStatusCode;

    public BinWalletException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.httpStatusCode = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public HttpStatusCode getHttpStatusCode() {
        return this.httpStatusCode;
    }
}
