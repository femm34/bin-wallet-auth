package com.fecd.bin_wallet_auth.users.repository;


import com.fecd.bin_wallet_auth.users.application.dtos.UserBasics;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u.id as id,
                   u.firstName as firstName,
                   u.lastName as lastName,
                   u.username as username,
                   u.email as email
                   FROM User u WHERE u.username = :username""")
    Optional<UserBasics> findUserByUsername(@Param("username") String username);

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM \"user\" u WHERE u.username = :username)", nativeQuery = true)
    boolean existsUserByUsername(@Param("username") String username);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM \"user\" u WHERE u.email = :email)", nativeQuery = true)
    boolean existsUserByEmail(@Param("email") String email);

    @Query("update User u set u.failedAttempts = :failAttempts where u.username = :username")
    @Modifying
    void updateFailedAttempts(int failAttempts, String username);
}
