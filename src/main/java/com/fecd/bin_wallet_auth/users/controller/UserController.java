package com.fecd.bin_wallet_auth.users.controller;

import com.fecd.bin_wallet_auth.shared.constants.ApiPathConstants;
import com.fecd.bin_wallet_auth.shared.constants.Routes;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.USERS_ROUTE)
public interface UserController {

    @PostMapping("/sign-up")
    ResponseEntity<BinWalletResponse> signUpUser(@RequestBody UserRequest userRequest);

    @PostMapping("/sign-in")
    ResponseEntity<BinWalletResponse> signInUser(@RequestBody UserRequestLogin userRequestLogin) throws AccountLockedException;

    @GetMapping("/hello")
    String hello();
}
