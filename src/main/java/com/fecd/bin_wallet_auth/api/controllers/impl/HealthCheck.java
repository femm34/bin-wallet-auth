package com.fecd.bin_wallet_auth.api.controllers.impl;

import com.fecd.bin_wallet_auth.api.controllers.IHealthCheck;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck implements IHealthCheck {
    @Override
    public ResponseEntity<BinWalletResponse> healthCheck() {
        return ResponseEntity.ok(BinWalletResponse.builder()
                .message("Auth microservice is up and running")
                .status(HttpStatus.OK)
                .data(null)
                .code(HttpStatus.OK.value())
                .build());
    }
}
