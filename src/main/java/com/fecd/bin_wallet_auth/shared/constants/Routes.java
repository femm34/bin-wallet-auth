package com.fecd.bin_wallet_auth.shared.constants;

public abstract class Routes {
    public static String[] WHITE_LIST = {
        ApiPathConstants.V1_ROUTE + ApiPathConstants.HEALTH + "/check",
        ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE + "/sign-in",
        ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE + "/sign-up",
        ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE + "/hello",
        ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE + "/reset-password",
        ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE + "/change-password",
        ApiPathConstants.V1_ROUTE + ApiPathConstants.USERS_ROUTE + "/delete"
    };
}
