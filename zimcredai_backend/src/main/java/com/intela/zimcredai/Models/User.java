package com.intela.zimcredai.Models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

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
        return true; // Or adjust according to your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or adjust according to your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or adjust according to your logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Or adjust according to your logic
    }
}
