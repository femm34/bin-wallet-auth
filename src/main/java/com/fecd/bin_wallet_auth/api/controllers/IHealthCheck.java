package com.fecd.bin_wallet_auth.api.controllers;

import com.fecd.bin_wallet_auth.shared.constants.ApiPathConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.HEALTH)
public interface IHealthCheck {
    @GetMapping("/check")
    ResponseEntity<?> healthCheck();
}
