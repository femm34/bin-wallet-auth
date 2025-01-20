package com.fecd.bin_wallet_auth.authorization.application.service;

import com.fecd.bin_wallet_auth.authorization.domain.dto.request.RoleRequest;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;

public interface RoleService {

    BinWalletResponse createRole(RoleRequest roleRequest);

    BinWalletResponse deleteRole();

}
