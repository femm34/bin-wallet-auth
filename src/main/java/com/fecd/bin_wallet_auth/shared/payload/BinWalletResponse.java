package com.fecd.bin_wallet_auth.shared.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinWalletResponse {
    private String message;
    private Object data;
    private HttpStatus status;
    private Integer code;
}
