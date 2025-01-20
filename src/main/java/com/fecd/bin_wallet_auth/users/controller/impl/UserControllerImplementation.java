package com.fecd.bin_wallet_auth.users.controller.impl;

import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.application.service.UserService;
import com.fecd.bin_wallet_auth.users.controller.UserController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserControllerImplementation implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<?> deleteUserById(HttpServletRequest request, HttpServletResponse response) {
        this.userService.deleteUserById(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(BinWalletResponse.builder()
                .status(HttpStatus.OK)
                .data("")
                .message("user deleted successfully")
                .code(HttpStatus.OK.value())
                .build());
    }
}
