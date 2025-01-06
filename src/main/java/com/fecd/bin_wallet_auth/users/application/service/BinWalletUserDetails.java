package com.fecd.bin_wallet_auth.users.application.service;

import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BinWalletUserDetails implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(User::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
