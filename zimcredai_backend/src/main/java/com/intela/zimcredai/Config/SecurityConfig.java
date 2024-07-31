package com.intela.zimcredai.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.intela.zimcredai.Models.Permission.*;
import static com.intela.zimcredai.Models.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomLogoutHandler logoutHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize)->authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Check if user has role ROLE_ADMIN
                        // Admin and Employee endpoints
                        .requestMatchers("/api/v1/endpoint-1/**").hasAnyRole(ADMIN.name(), COORDINATOR.name(), CUSTOMER.name())
                        .requestMatchers("/api/v1/endpoint-2/**").hasAnyRole(ADMIN.name(), COORDINATOR.name(), CUSTOMER.name())
                        // endpoint n .. n+1

                        // Check if user has authorities to create, read or delete
                        // Admin CRUD operations
                        .requestMatchers(POST, "/api/v1/endpoint-1/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/endpoint-1/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/endpoint-1/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers(DELETE, "/api/v1/endpoint-1/**").hasAuthority(ADMIN_READ.name())

                        .requestMatchers(POST, "/api/v1/endpoint-2/**").hasAuthority(ADMIN_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/endpoint-2/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/endpoint-2/**").hasAuthority(ADMIN_DELETE.name())
                        .requestMatchers(DELETE, "/api/v1/endpoint-2/**").hasAuthority(ADMIN_READ.name())
                        // endpoint n .. n+1

                        .anyRequest().authenticated()
                )
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((logout) -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((
                                (request, response, authentication) ->
                                        SecurityContextHolder.clearContext()
                        ))
                );
        return http.build();
    }
}
