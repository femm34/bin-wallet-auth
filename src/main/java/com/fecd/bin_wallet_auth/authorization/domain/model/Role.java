package com.fecd.bin_wallet_auth.authorization.domain.model;

import com.fecd.bin_wallet_auth.sharedKernel.domain.AbstractAuditingEntity;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends AbstractAuditingEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "users")
    private Set<User> users = new HashSet<>();
}
