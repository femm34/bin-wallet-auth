package com.fecd.bin_wallet_auth.users.domain.repository;


import com.fecd.bin_wallet_auth.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
