package com.fecd.bin_wallet_auth.authorization.repository;

import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.name = :name")
    Optional<Role> findRoleByName(@Param("name") String name);

    @Query("select r from Role r where r.name = :name")
    boolean existsRole(@Param("name") String name);
}
