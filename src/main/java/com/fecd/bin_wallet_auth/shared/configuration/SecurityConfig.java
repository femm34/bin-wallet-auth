package com.fecd.bin_wallet_auth.shared.configuration;

import com.fecd.bin_wallet_auth.authentication.infraestructure.configuration.AuthenticationConfig;
import com.fecd.bin_wallet_auth.users.domain.application.service.BinWalletUserDetails;
import jakarta.servlet.FilterChain;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final BinWalletUserDetails userDetails;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c ->
                        c.requestMatchers("api/v1/health/check").permitAll().anyRequest().authenticated())
                .authenticationProvider(new AuthenticationConfig(this.userDetails).authenticationProvider())
                .build();
    }
}
