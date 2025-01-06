package com.fecd.bin_wallet_auth.users.service;

import com.fecd.bin_wallet_auth.users.domain.model.User;
import com.fecd.bin_wallet_auth.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

@AllArgsConstructor
@Service
@Transactional
public class UserFailedAttempts {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = Duration.ofMinutes(2).toMillis();

    private final UserRepository userRepository;


    public void updateFailedAttempts(User user) {
        int updateFailedAttempts = user.getFailedAttempts() + 1;
        this.userRepository.updateFailedAttempts(updateFailedAttempts, user.getUsername());
    }


    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(LocalDateTime.now());
        this.userRepository.save(user);
    }

    public void resetFailedAttempts(User user) {
        this.userRepository.updateFailedAttempts(0, user.getUsername());
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeMilliseconds = user.getLockTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long currentTimeMilliseconds = System.currentTimeMillis();
        if (lockTimeMilliseconds + LOCK_TIME_DURATION < currentTimeMilliseconds) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempts(0);
            this.userRepository.save(user);
            return true;
        }
        return false;
    }



}
