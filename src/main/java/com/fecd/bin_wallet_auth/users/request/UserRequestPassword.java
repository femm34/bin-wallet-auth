package com.fecd.bin_wallet_auth.users.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class UserRequestPassword {

    private String token;

    private String password;
}
