package com.fecd.bin_wallet_auth.users.service;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import com.fecd.bin_wallet_auth.users.request.UserRequestPassword;
import com.fecd.bin_wallet_auth.users.request.UserRequestToken;
import jakarta.servlet.http.HttpServletResponse;

import javax.security.auth.login.AccountLockedException;

public interface UserServiceAuth {

    User signUpUser(UserRequest userRequest);

    JWTResponse signInUser(UserRequestLogin userRequestLogin, HttpServletResponse response) throws AccountLockedException;

    BinWalletResponse changePassword(UserRequestPassword userRequestPassword);

    public void resetPassword(UserRequestToken userRequestToken);

}
