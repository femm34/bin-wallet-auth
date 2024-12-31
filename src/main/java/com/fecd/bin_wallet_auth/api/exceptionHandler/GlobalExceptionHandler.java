package com.fecd.bin_wallet_auth.api.exceptionHandler;

import com.fecd.bin_wallet_auth.shared.exceptions.BinWalletException;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BinWalletException.class)
    public ResponseEntity<BinWalletResponse> handleBinWalletException(BinWalletException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(BinWalletResponse.builder()
                .message(ex.getMessage())
                .data(null)
                .code(ex.getHttpStatusCode().value())
                .status(ex.getHttpStatus())
                .build());

    };
}
