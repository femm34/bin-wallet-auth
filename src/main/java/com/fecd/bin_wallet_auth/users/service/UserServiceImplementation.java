package com.fecd.bin_wallet_auth.users.service;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.authentication.application.service.IJWTService;
import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import com.fecd.bin_wallet_auth.authorization.repository.RoleRepository;
import com.fecd.bin_wallet_auth.shared.constants.dts.TokenType;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.exceptions.UserEmailAlreadyTakenException;
import com.fecd.bin_wallet_auth.users.exceptions.UsernameAlreadyTakenException;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import com.fecd.bin_wallet_auth.users.request.UserRequestPassword;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Set;

import static com.fecd.bin_wallet_auth.users.service.UserFailedAttemptsService.MAX_FAILED_ATTEMPTS;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authentication;
    private final IJWTService jwtService;
    public static final long LOCK_TIME_DURATION = 2;

    @Override
    public User signUpUser(UserRequest userRequest) {
        Role existingRole = this.roleRepository.findRoleByName(userRequest.getRole())
                .orElseGet(() -> {
                    Role newRole = Role.builder()
                            .name(userRequest.getRole())
                            .description("user is " + userRequest.getRole())
                            .build();
                    return this.roleRepository.save(newRole);
                });

        if (this.userRepository.existsUserByUsername(userRequest.getUsername())) {
            throw new UsernameAlreadyTakenException("username is already taken");
        }

        if (this.userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new UserEmailAlreadyTakenException("email is already taken");
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .username(userRequest.getUsername())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .roles(Set.of(existingRole))
                .active(true)
                .accountNonLocked(true)
                .build();
        return this.userRepository.save(user);
    }

    @Override
    public JWTResponse signInUser(UserRequestLogin userRequestLogin) throws AccountLockedException {
        User user = this.userRepository.findByUsername(userRequestLogin.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid user. Try again."));

        if (!user.isAccountNonLocked()) {
            LocalDateTime unlockTime = user.getLockTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");


            if (LocalDateTime.now().isBefore(unlockTime)) {
                Duration minutesLeft = Duration.between(LocalDateTime.now(), user.getLockTime());

                throw new AccountLockedException("Your account is locked. Try again in " + String.format("%02d:%02d",
                        minutesLeft.toMinutes(), minutesLeft.toSeconds() % 60) + " minutes.");
            } else {
                user.setAccountNonLocked(true);
                user.setFailedAttempts(0);
                user.setLockTime(null);
                this.userRepository.save(user);
            }
        }

        try {
            Authentication authentication = this.authentication.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestLogin.getUsername(),
                            userRequestLogin.getPassword()));

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getId(),
                        Duration.ofMinutes(5).toMillis(),
                        TokenType.ACCESS_TOKEN.toString(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getRoles()
                );
            }
        } catch (BadCredentialsException ex) {
            int failedAttempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(failedAttempts);

            if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                user.setLockTime(LocalDateTime.now().plusMinutes(LOCK_TIME_DURATION));
                this.userRepository.save(user);

                throw new AccountLockedException("Your account is locked due to too many failed attempts. Try again " +
                        "later.");
            }

            this.userRepository.save(user);
            throw new BadCredentialsException("Invalid password. Try again.");
        }
        return null;
    }

    @Override
    public BinWalletResponse resetPassword(UserRequestPassword userRequestPassword) {
        return null;
    }
}
