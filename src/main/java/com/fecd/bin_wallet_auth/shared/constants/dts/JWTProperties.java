package com.fecd.bin_wallet_auth.shared.constants.dts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JWTProperties {

    @Value("${jwt.secret}")
    private String JWT_SECRET;
}
