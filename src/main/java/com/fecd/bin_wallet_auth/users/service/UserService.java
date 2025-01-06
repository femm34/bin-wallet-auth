package com.fecd.bin_wallet_auth.users.service;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import com.fecd.bin_wallet_auth.users.request.UserRequestPassword;

import javax.security.auth.login.AccountLockedException;

public interface UserService {

    User signUpUser(UserRequest userRequest);

    JWTResponse signInUser(UserRequestLogin userRequestLogin) throws AccountLockedException;

    BinWalletResponse resetPassword(UserRequestPassword userRequestPassword);


}
