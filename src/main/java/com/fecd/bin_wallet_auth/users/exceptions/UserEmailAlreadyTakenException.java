package com.fecd.bin_wallet_auth.users.exceptions;

public class UserEmailAlreadyTakenException extends RuntimeException{

    public UserEmailAlreadyTakenException(String message) {
        super(message);
    }
}
