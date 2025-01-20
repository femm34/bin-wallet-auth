package com.fecd.bin_wallet_auth.users.service;

import com.fecd.bin_wallet_auth.authentication.application.dtos.JWTResponse;
import com.fecd.bin_wallet_auth.authentication.application.service.IJWTService;
import com.fecd.bin_wallet_auth.authorization.domain.model.PasswordResetToken;
import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import com.fecd.bin_wallet_auth.authorization.exeptions.PasswordResetTokenExpiredException;
import com.fecd.bin_wallet_auth.authorization.repository.RoleRepository;
import com.fecd.bin_wallet_auth.shared.constants.dts.TokenStatus;
import com.fecd.bin_wallet_auth.shared.constants.dts.TokenType;
import com.fecd.bin_wallet_auth.shared.exceptions.BinWalletException;
import com.fecd.bin_wallet_auth.shared.payload.BinWalletResponse;
import com.fecd.bin_wallet_auth.shared.utils.CookieUtil;
import com.fecd.bin_wallet_auth.users.domain.model.Email;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.exceptions.UserEmailAlreadyTakenException;
import com.fecd.bin_wallet_auth.users.exceptions.UsernameAlreadyTakenException;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import com.fecd.bin_wallet_auth.users.request.UserRequest;
import com.fecd.bin_wallet_auth.users.request.UserRequestLogin;
import com.fecd.bin_wallet_auth.users.request.UserRequestPassword;
import com.fecd.bin_wallet_auth.users.request.UserRequestToken;
import com.fecd.bin_wallet_auth.users.service.email.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static com.fecd.bin_wallet_auth.users.service.UserFailedAttemptsService.MAX_FAILED_ATTEMPTS;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceAuthImplementation implements UserServiceAuth {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authentication;
    private final IJWTService jwtService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    public static final long LOCK_TIME_DURATION = 2;
    private final CookieUtil cookieUtil;
    private final Duration TOKEN_EXPIRE_TIME = Duration.ofMinutes(5);

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
    public JWTResponse signInUser(UserRequestLogin userRequestLogin, HttpServletResponse response) throws AccountLockedException {
        User user = this.userRepository.findByUsername(userRequestLogin.getUsername())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid user. Try again."));

        if (!user.isAccountNonLocked()) {
            LocalDateTime unlockTime = user.getLockTime();

            if (LocalDateTime.now().isBefore(unlockTime)) {
                Duration minutesLeft = Duration.between(LocalDateTime.now(), user.getLockTime());

                throw new AccountLockedException("Your account is locked. Try again in " +
                        String.format("%02d:%02d", minutesLeft.toMinutes(), minutesLeft.toSeconds() % 60) + " minutes" +
                        ".");
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
                String jwtToken = jwtService.generateToken(
                        user.getId(),
                        Duration.ofMinutes(30).toMillis(),
                        TokenType.ACCESS_TOKEN.toString(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getRoles()
                ).getToken();

                Cookie jwtCookie = new Cookie("access_token",jwtToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                response.addCookie(jwtCookie);

                return new JWTResponse(jwtToken);
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
    public BinWalletResponse changePassword(UserRequestPassword userRequestPassword) {
        User user = this.userRepository.findUserByResetPasswordToken(userRequestPassword.getToken()).orElseThrow();
        boolean isValidToken = this.passwordResetService.validateResetPasswordToken(userRequestPassword.getToken(),
                user.getUsername());

        if (user != null) {
            user.setPassword(this.passwordEncoder.encode(userRequestPassword.getPassword()));
            this.userRepository.save(user);
        }

        return BinWalletResponse.builder()
                .code(HttpStatus.OK.value())
                .data("password reset successfully!")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public void resetPassword(UserRequestToken userRequestToken) {
        User userToResetPassword = this.userRepository.findByEmail(userRequestToken.getEmail())
                .orElseThrow(() -> new BinWalletException("email does not exists!"));

        String token = jwtService.generateToken(userToResetPassword.getId(),
                        TOKEN_EXPIRE_TIME.toMillis(),
                        TokenType.ACCESS_TOKEN.toString(),
                        userToResetPassword.getUsername(),
                        userToResetPassword.getFirstName(),
                        userToResetPassword.getRoles())
                .getToken();

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .expireDate(LocalDateTime.now().plusMinutes(5))
                .status(TokenStatus.ACTIVE)
                .build();

        userToResetPassword.addPasswordResetToken(passwordResetToken);
        this.userRepository.save(userToResetPassword);

        Email emailToSend = Email.builder()
                .recipient(userToResetPassword.getEmail())
                .subject("Token to reset your password")
                .text("The toke generated was: " + token + "\n" + "The toke will expire in " + String.format("%02d " +
                                "minutes",
                        TOKEN_EXPIRE_TIME.toMinutes()))
                .build();

        this.emailService.sendEmail(emailToSend);
    }
}
