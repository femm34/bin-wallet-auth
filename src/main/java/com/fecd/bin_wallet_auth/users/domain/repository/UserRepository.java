package com.fecd.bin_wallet_auth.users.domain.repository;


import com.fecd.bin_wallet_auth.users.domain.application.dtos.UserProjection;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.id as id, u.firstName as firstName, u.lastName as lastName, u.username as username, u.email as email FROM User u WHERE u.username = :username")
    Optional<UserProjection> findUserByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);
}
