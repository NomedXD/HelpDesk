package com.innowise.domain;

import com.innowise.security.entities.Token;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
@Table(name = "users")
@Entity
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "user_role")
    @Enumerated
    private UserRole role;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Ticket> ticketsAsAssignee;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Ticket> ticketsAsOwner;

    @OneToMany(mappedBy = "approver", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Ticket> ticketsAsApprover;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<History> history;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Feedback> feedback;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Comment> comments;

    @Transient
    private Token token;

    @Transient
    private Boolean isAccountNonExpired = true;

    @Transient
    private Boolean isAccountNonLocked = true;

    @Transient
    private Boolean isCredentialsNonExpired = true;

    @Transient
    private Boolean isEnabled = true;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
