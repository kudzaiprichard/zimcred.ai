package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.intela.zimcredai.Models.Permission.*;

@RequiredArgsConstructor
public enum Role {
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_READ,
                    COORDINATOR_CREATE,
                    COORDINATOR_UPDATE,
                    COORDINATOR_DELETE,
                    COORDINATOR_READ
            )
    ),
    COORDINATOR(
            Set.of(
                    COORDINATOR_CREATE,
                    COORDINATOR_UPDATE,
                    COORDINATOR_DELETE,
                    COORDINATOR_READ
            )
    ),

    CUSTOMER(
            Set.of(
                    CUSTOMER_CREATE,
                    CUSTOMER_UPDATE,
                    CUSTOMER_DELETE,
                    CUSTOMER_READ
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
