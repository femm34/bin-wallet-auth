package com.fecd.bin_wallet_auth.authorization.domain.mapper;

import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    private RoleMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Collection<GrantedAuthority> toGrantedAuthority(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    ;

    public static List<String> toRolesString(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }


}
