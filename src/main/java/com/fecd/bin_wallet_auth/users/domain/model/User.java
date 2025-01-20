package com.fecd.bin_wallet_auth.users.domain.model;

import com.fecd.bin_wallet_auth.authentication.domain.model.BinWalletUserDetails;
import com.fecd.bin_wallet_auth.authorization.domain.model.PasswordResetToken;
import com.fecd.bin_wallet_auth.authorization.domain.model.RefreshToken;
import com.fecd.bin_wallet_auth.authorization.domain.model.Role;
import com.fecd.bin_wallet_auth.sharedKernel.domain.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "\"user\"")
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

    private boolean accountNonLocked;

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

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private RefreshToken refreshToken;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<PasswordResetToken> resetTokens;

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    public void addPasswordResetToken(PasswordResetToken passwordResetToken) {
        this.resetTokens.add(passwordResetToken);
        passwordResetToken.setUser(this);
    }
}
