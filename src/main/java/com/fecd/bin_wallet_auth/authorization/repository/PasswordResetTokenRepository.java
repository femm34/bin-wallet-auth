package com.fecd.bin_wallet_auth.authorization.repository;

import com.fecd.bin_wallet_auth.authorization.domain.model.PasswordResetToken;
import com.fecd.bin_wallet_auth.shared.constants.dts.TokenStatus;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import io.jsonwebtoken.Jwts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM password_reset_token prt WHERE prt.token = :token)", nativeQuery = true)
    boolean isTokenFound(@Param("token") String token);

    @Query(value = "SELECT prt.expire_date FROM password_reset_token prt WHERE prt.token LIKE :token", nativeQuery =
            true)
    LocalDateTime findPasswordResetTokenExpireDate(@Param("token") String token);

    @Query(value = "SELECT COUNT(1) > 0 FROM password_reset_token prt WHERE prt.token = :token AND prt.status ILIKE " +
            "'%active%'", nativeQuery = true)
    boolean isValidToken(@Param("token") String token);

    @Query(value = "UPDATE password_reset_token SET status = :status WHERE token = :token", nativeQuery = true)
    void updateTokenStatus(@Param("token") String token, @Param("status") String status);

    @Query(value = """
            SELECT *
            FROM password_reset_token 
            WHERE user_id = (SELECT id FROM "user" WHERE username = :username) AND status ILIKE 'ACTIVE';
            """, nativeQuery = true)
    PasswordResetToken findTokenByUserUsername(@Param("username") String username);

    String user(User user);
}
