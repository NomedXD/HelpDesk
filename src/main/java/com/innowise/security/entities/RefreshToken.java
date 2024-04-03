package com.innowise.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Check;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "refresh_tokens")
@Check(constraints = "expires_at > now()")
public class RefreshToken {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", unique = true)
    private Integer userId;

    @Column(name = "expires_at")
    private Instant expiresAt;
}
