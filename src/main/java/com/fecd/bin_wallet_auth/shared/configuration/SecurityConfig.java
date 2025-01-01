package com.fecd.bin_wallet_auth.shared.configuration;

import com.fecd.bin_wallet_auth.authentication.infraestructure.configuration.AuthenticationConfig;
import com.fecd.bin_wallet_auth.authentication.infraestructure.configuration.CORSConfig;
import com.fecd.bin_wallet_auth.shared.constants.ApiPathConstants;
import com.fecd.bin_wallet_auth.shared.constants.Routes;
import com.fecd.bin_wallet_auth.users.domain.application.service.BinWalletUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final BinWalletUserDetails userDetails;

    private void logout(LogoutConfigurer<HttpSecurity> logoutCustomizer) {
        logoutCustomizer.logoutUrl(ApiPathConstants.V1_ROUTE + ApiPathConstants.AUTH_ROUTE + "/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                });
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(CORSConfig.corsConfigurationSource()))
                .authorizeHttpRequests(c -> c.requestMatchers(Routes.WHITE_LIST).permitAll().anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(this::logout)
                .authenticationProvider(new AuthenticationConfig(this.userDetails).authenticationProvider())
                .build();
    }
}
