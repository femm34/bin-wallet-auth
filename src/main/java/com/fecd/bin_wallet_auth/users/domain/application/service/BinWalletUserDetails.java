package com.fecd.bin_wallet_auth.users.domain.application.service;

import com.fecd.bin_wallet_auth.authorization.domain.mapper.RoleMapper;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.domain.repository.UserRepository;
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
        User userFound = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(userFound.getUsername())
                .password(userFound.getPassword())
                .authorities(RoleMapper.toGrantedAuthority(userFound.getRoles()))
                .build();
    }
}
