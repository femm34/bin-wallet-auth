package com.fecd.bin_wallet_auth.authentication.application.dtos;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class JWTResponse {
    String token;
}
