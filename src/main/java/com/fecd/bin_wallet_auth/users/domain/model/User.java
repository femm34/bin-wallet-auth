package com.fecd.bin_wallet_auth.users.domain.model;

import com.fecd.bin_wallet_auth.authentication.domain.model.BinWalletUserDetails;
import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import com.fecd.bin_wallet_auth.sharedKernel.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends AbstractAuditingEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String username;

    private String email;

    private String password;

    @Column(name = "active", nullable = false)
    @ColumnDefault("true")
    private boolean active;

    private LocalDateTime lockTime;

    @Column(name = "failed_attempts")
    private int failedAttempts = 0;

    public UserDetails toUserDetails() {
        return new BinWalletUserDetails(this);
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
}
