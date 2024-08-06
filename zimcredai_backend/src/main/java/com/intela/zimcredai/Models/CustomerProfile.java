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
@Entity(name = "customer_profiles")
public class CustomerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer creditScore;
    @Enumerated(EnumType.STRING)
    @Transient
    private RiskLevel riskLevel;
    private Integer totalLoans;
    private Integer totalDefaults;
    private Integer totalRepayments;
    private Float averageRepaymentsTime;
    private Integer defaultRate;

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

    @PostLoad
    public void updateRiskLevel() {
        if (creditScore == null || totalLoans == null || totalDefaults == null || averageRepaymentsTime == null || defaultRate == null) {
            this.riskLevel = RiskLevel.UNASSIGNED;
            return;
        }

        // Define thresholds based on quantifiable metrics
        final int LOW_CREDIT_SCORE_THRESHOLD = 750;
        final int MEDIUM_CREDIT_SCORE_THRESHOLD = 600;
        final int LOW_DEFAULT_RATE_THRESHOLD = 5;
        final int MEDIUM_DEFAULT_RATE_THRESHOLD = 10;
        final int LOW_AVERAGE_REPAYMENT_TIME_THRESHOLD = 30;
        final int MEDIUM_AVERAGE_REPAYMENT_TIME_THRESHOLD = 60;
        final int VERY_HIGH_DEFAULT_RATE_THRESHOLD = 20;
        final int VERY_HIGH_AVERAGE_REPAYMENT_TIME_THRESHOLD = 90;

        // Risk Level Classification
        if (creditScore > LOW_CREDIT_SCORE_THRESHOLD &&
                defaultRate < LOW_DEFAULT_RATE_THRESHOLD &&
                averageRepaymentsTime < LOW_AVERAGE_REPAYMENT_TIME_THRESHOLD) {
            this.riskLevel = RiskLevel.LOW;
        } else if (creditScore > MEDIUM_CREDIT_SCORE_THRESHOLD &&
                defaultRate < MEDIUM_DEFAULT_RATE_THRESHOLD &&
                averageRepaymentsTime < MEDIUM_AVERAGE_REPAYMENT_TIME_THRESHOLD) {
            this.riskLevel = RiskLevel.MEDIUM;
        } else if (creditScore <= MEDIUM_CREDIT_SCORE_THRESHOLD &&
                defaultRate >= MEDIUM_DEFAULT_RATE_THRESHOLD &&
                averageRepaymentsTime >= MEDIUM_AVERAGE_REPAYMENT_TIME_THRESHOLD) {
            this.riskLevel = RiskLevel.HIGH;
        } else if (creditScore < MEDIUM_CREDIT_SCORE_THRESHOLD ||
                defaultRate >= VERY_HIGH_DEFAULT_RATE_THRESHOLD ||
                averageRepaymentsTime >= VERY_HIGH_AVERAGE_REPAYMENT_TIME_THRESHOLD) {
            this.riskLevel = RiskLevel.VERY_HIGH;
        } else {
            this.riskLevel = RiskLevel.UNASSIGNED;
        }
    }

}
