package com.intela.zimcredai.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "customers")
public class Customer {
    /**
     *  Todo: Make sure to capture necessary user data to use for ml predictions
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String profileImage; // base64
    private String backgroundImage; // base64
    // Get all customers details for prediction
    private Boolean sex; // 1 Male / 0 Female
    private String address; // Location
    private LocalDate dateOfBirth;
    private Boolean maritalStatus; // 1 Married / 0 not Married
    private Boolean isEmployed;
    private Float salaryAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
}
