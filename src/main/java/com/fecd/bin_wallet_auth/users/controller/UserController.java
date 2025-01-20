package com.fecd.bin_wallet_auth.users.controller;

import com.fecd.bin_wallet_auth.shared.constants.ApiPathConstants;
import com.fecd.bin_wallet_auth.shared.constants.Routes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.USERS_ROUTE)
public interface UserController {

    @DeleteMapping("/delete")
    ResponseEntity<?> deleteUserById(HttpServletRequest request, HttpServletResponse response);
}
