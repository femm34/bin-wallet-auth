package com.fecd.bin_wallet_auth.api.exceptionHandler;

import com.fecd.bin_wallet_auth.shared.exceptions.BinWalletException;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.exceptions.UserEmailAlreadyTakenException;
import com.fecd.bin_wallet_auth.users.exceptions.UsernameAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.auth.login.AccountLockedException;

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

    }

    ;

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BinWalletResponse> handleUserNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(BinWalletResponse.builder()
                        .message(ex.getMessage())
                        .data(null)
                        .code(404)
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<BinWalletResponse> handleUsernameAlreadyTakenExpetion(UsernameAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BinWalletResponse.builder()
                        .message(ex.getMessage())
                        .data(null)
                        .code(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .build());
    }

    @ExceptionHandler(UserEmailAlreadyTakenException.class)
    public ResponseEntity<BinWalletResponse> handleUserEmailAlreadyTakenException(UserEmailAlreadyTakenException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BinWalletResponse.builder()
                        .message(ex.getMessage())
                        .data(null)
                        .code(HttpStatus.CONFLICT.value())
                        .status(HttpStatus.CONFLICT)
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BinWalletResponse> handleBadCredentialException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BinWalletResponse.builder()
                        .message(ex.getMessage())
                        .data(null)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<BinWalletResponse> handleBadCredentialException(AccountLockedException ex) {
        return ResponseEntity.status(HttpStatus.LOCKED)
                .body(BinWalletResponse.builder()
                        .message(ex.getMessage())
                        .data(null)
                        .code(HttpStatus.LOCKED.value())
                        .status(HttpStatus.LOCKED)
                        .build());

    }
}