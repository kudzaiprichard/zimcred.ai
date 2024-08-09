package com.intela.zimcredai.Models;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Getter(AccessLevel.NONE)
    private Boolean isEnabled;
    @Getter(AccessLevel.NONE)
    private Boolean isCredentialsNonExpired;
    @Getter(AccessLevel.NONE)
    private Boolean isAccountNonLocked;
    @Getter(AccessLevel.NONE)
    private Boolean isAccountNonExpired;
    private Boolean isAccountVerified; // verify via email

    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.emptyList(); // Or handle default authorities
        }
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isAccountNonExpired; // Or adjust according to your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked; // Or adjust according to your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired; // Or adjust according to your logic
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled; // Or adjust according to your logic
    }
}
