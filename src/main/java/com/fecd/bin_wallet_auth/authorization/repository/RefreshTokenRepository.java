package com.fecd.bin_wallet_auth.authorization.repository;

import com.fecd.bin_wallet_auth.authorization.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("select t from RefreshToken t where t.token = :token")
    Optional<RefreshToken> findTokenByToken(@Param("token") String token);
}
