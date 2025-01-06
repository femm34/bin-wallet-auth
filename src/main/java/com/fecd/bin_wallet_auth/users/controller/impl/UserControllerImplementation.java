package com.fecd.bin_wallet_auth.users.controller.impl;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.application.dtos.UserDto;
import com.fecd.bin_wallet_auth.users.controller.UserController;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import com.fecd.bin_wallet_auth.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountLockedException;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class UserControllerImplementation implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<BinWalletResponse> signUpUser(UserRequest userRequest) {
        User userToSingUp = this.userService.signUpUser(userRequest);
        UserDto userResponse = new UserDto(userToSingUp.getFirstName(),
                userToSingUp.getLastName(),
                userToSingUp.getUsername(),
                userToSingUp.getEmail(),
                userToSingUp.getRoles().stream().map(String::valueOf).collect(Collectors.toSet()),
                userToSingUp.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BinWalletResponse("User registered successfully", userResponse, HttpStatus.CREATED,
                        HttpStatusCode.valueOf(201).value()));
    }

    @Override
    public ResponseEntity<BinWalletResponse> signInUser(UserRequestLogin userRequestLogin) throws AccountLockedException {
        JWTResponse token = this.userService.signInUser(userRequestLogin);
        return ResponseEntity.status(HttpStatus.OK).body(new BinWalletResponse("Given token", token, HttpStatus.OK,
                HttpStatus.valueOf(200).value()));
    }

    @Override
    public String hello() {
        return "Hello you are authenticated";
    }
}
