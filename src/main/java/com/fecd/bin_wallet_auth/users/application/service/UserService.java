package com.fecd.bin_wallet_auth.users.application.service;

import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.application.dtos.UserBasics;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    void deleteUserById(HttpServletRequest request, HttpServletResponse response);

    BinWalletResponse updateUserById(HttpServletRequest request);

    UserBasics getUserById(HttpServletRequest request);

    List<UserBasics> getAllUsers();
}
