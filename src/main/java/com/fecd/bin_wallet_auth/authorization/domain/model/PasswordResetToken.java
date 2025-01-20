package com.fecd.bin_wallet_auth.authorization.domain.model;

import com.fecd.bin_wallet_auth.shared.constants.dts.TokenStatus;
import com.fecd.bin_wallet_auth.users.domain.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    private LocalDateTime expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
