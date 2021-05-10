package com.epam.esm.model;

import com.epam.esm.audit.EntityActionListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Entity
@EntityListeners(EntityActionListener.class)
@Table(name = "users")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User implements Model, UserDetails {
    private static final int HASH_CODE = 19;
    @Id
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    private Long id;
    @Column(name = "login", length = 64, unique = true, nullable = false)
    private String login;
    @Column(name = "password", length = 64, nullable = false)
    private String password;
    @Column(name = "removed", columnDefinition = "boolean default false")
    private boolean removed;
    @Column(name = "is_account_non_expired", columnDefinition = "boolean default true")
    private boolean isAccountNonExpired;
    @Column(name = "is_account_non_locked", columnDefinition = "boolean default true")
    private boolean isAccountNonLocked;
    @Column(name = "is_credentials_non_expired", columnDefinition = "boolean default true")
    private boolean isCredentialsNonExpired;
    @Column(name = "is_enabled", columnDefinition = "boolean default true")
    private boolean isEnabled;
    private Set<? extends GrantedAuthority> authorities;

    public User(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("login='" + login + "'")
                .add("password='" + password + "'")
                .add("removed=" + removed)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return HASH_CODE;
    }
}
