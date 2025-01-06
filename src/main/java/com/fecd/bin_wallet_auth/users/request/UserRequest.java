package com.fecd.bin_wallet_auth.users.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserRequest {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String role;

    private String password;

}
