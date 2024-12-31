package com.fecd.bin_wallet_auth.users.domain.application.dtos;

public interface UserProjection {
    Long getId();
    String getFirstName();
    String getLastName();
    String getUsername();
    String getEmail();
}
