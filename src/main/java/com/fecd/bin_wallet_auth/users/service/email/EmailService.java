package com.fecd.bin_wallet_auth.users.service.email;

import com.fecd.bin_wallet_auth.users.domain.model.Email;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import org.springframework.stereotype.Service;

public interface EmailService {

    void sendEmail(Email email);

}
