package com.fecd.bin_wallet_auth.authentication.infraestructure.configuration;

import com.fecd.bin_wallet_auth.users.application.service.BinWalletUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class AuthenticationConfig {
    private final BinWalletUserDetails userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public  AuthenticationProvider authenticationProvider() {
           DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
           daoAuthenticationProvider.setUserDetailsService(userDetailsService);
           daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
           return daoAuthenticationProvider;
    }
}
