package com.fecd.bin_wallet_auth.users.controller;

import com.fecd.bin_wallet_auth.shared.constants.ApiPathConstants;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import com.fecd.bin_wallet_auth.users.request.UserRequestPassword;
import com.fecd.bin_wallet_auth.users.request.UserRequestToken;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.login.AccountLockedException;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE)
public interface UserAuthController {

    @PostMapping("/sign-up")
    ResponseEntity<BinWalletResponse> signUpUser(@RequestBody UserRequest userRequest);

    @PostMapping("/sign-in")
    ResponseEntity<BinWalletResponse> signInUser(@RequestBody UserRequestLogin userRequestLogin, HttpServletResponse response) throws AccountLockedException;

    @GetMapping("/hello")
    String hello();

    @PostMapping("/reset-password")
    ResponseEntity<BinWalletResponse> resetPassword(@RequestBody UserRequestToken userRequestToken);

    @PostMapping("/change-password")
    ResponseEntity<BinWalletResponse> changePassword(@RequestBody UserRequestPassword userRequestPassword);
}
