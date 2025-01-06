package com.fecd.bin_wallet_auth.shared.constants.dts;

public enum TokenStatus {
    ACTIVE("active"),
    EXPIRED("expired");

    private final String value;

    public String getValue() {
        return value;
    }

    TokenStatus(String value) {
        this.value = value;
    }
}
