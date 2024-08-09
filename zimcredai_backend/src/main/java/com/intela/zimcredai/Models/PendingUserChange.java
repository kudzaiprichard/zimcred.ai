package com.intela.zimcredai.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "pending_user_changes")
public class PendingUserChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Boolean isExpired;
    private String pendingEmail;
    private String pendingPassword;
    private String token;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDateTime tokenExpiry;
    private LocalDateTime createdAt;

    // Getters and Setters
}

