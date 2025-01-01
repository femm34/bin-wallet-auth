package com.fecd.bin_wallet_auth.authentication.application.dtos;

import lombok.*;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Builder
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class JwtAuthDetails {
    private final
    WebAuthenticationDetails webAuthenticationDetailsSource;
    private final String jwtToken;
}


