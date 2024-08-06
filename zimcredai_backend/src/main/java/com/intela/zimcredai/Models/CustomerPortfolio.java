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
@Entity(name = "customer_portfolios")
public class CustomerPortfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Transient
    private PortfolioCategory portfolioCategory;

    private Integer totalLoans;
    private Integer defaultCount;
    private Integer successfulRepayments;
    private Integer totalAmountBorrowed;
    private Integer totalAmountRepaid;

    @Enumerated(EnumType.STRING)
    @Transient
    private PortfolioClassification portfolioClassification;


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
    private void onLoad() {
        updatePortfolioCategory();
        updatePortfolioClassification();
    }

    public void updatePortfolioClassification() {
        // Check if essential metrics are available
        if (totalAmountBorrowed == null || totalAmountRepaid == null || totalLoans == null || successfulRepayments == null || defaultCount == null) {
            this.portfolioClassification = PortfolioClassification.UNASSIGNED;
            return;
        }

        // Calculate the repayment ratio
        double repaymentRatio = (double) totalAmountRepaid / totalAmountBorrowed;

        // Define thresholds for classification based on industry standards or data analysis
        final double EXCELLENT_REPAYMENT_RATIO_THRESHOLD = 1.0;
        final int EXCELLENT_SUCCESSFUL_REPAYMENTS_THRESHOLD = 5;
        final double GOOD_REPAYMENT_RATIO_THRESHOLD = 0.8;
        final int GOOD_DEFAULT_COUNT_THRESHOLD = 1;
        final int AVERAGE_DEFAULT_COUNT_THRESHOLD = 3;

        // Risk Level Classification
        if (repaymentRatio >= EXCELLENT_REPAYMENT_RATIO_THRESHOLD &&
                successfulRepayments > EXCELLENT_SUCCESSFUL_REPAYMENTS_THRESHOLD) {
            this.portfolioClassification = PortfolioClassification.EXCELLENT;
        } else if (repaymentRatio >= GOOD_REPAYMENT_RATIO_THRESHOLD &&
                defaultCount <= GOOD_DEFAULT_COUNT_THRESHOLD) {
            this.portfolioClassification = PortfolioClassification.GOOD;
        } else if (defaultCount <= AVERAGE_DEFAULT_COUNT_THRESHOLD) {
            this.portfolioClassification = PortfolioClassification.AVERAGE;
        } else {
            this.portfolioClassification = PortfolioClassification.POOR;
        }
    }


    public void updatePortfolioCategory() {
        // Ensure essential metrics are available
        if (totalAmountBorrowed == null || totalLoans == null || totalAmountRepaid == null) {
            this.portfolioCategory = PortfolioCategory.UNASSIGNED;
            return;
        }

        double spendingRatio = (double) totalAmountBorrowed / totalAmountRepaid;

        // Define thresholds for classification
        final double HIGH_SPENDING_THRESHOLD = 1.5; // Example threshold for high spending
        final double MODERATE_SPENDING_THRESHOLD = 1.0; // Example threshold for moderate spending
        final double LOW_SPENDING_THRESHOLD = 0.5; // Example threshold for low spending
        final double VERY_LOW_SPENDING_THRESHOLD = 0.2; // Example threshold for very low spending

        final double HIGH_BORROWING_THRESHOLD = 100000; // Example threshold for high borrowing
        final double LOW_BORROWING_THRESHOLD = 50000; // Example threshold for low borrowing

        // Calculate and assign category based on thresholds
        if (spendingRatio >= HIGH_SPENDING_THRESHOLD && totalAmountBorrowed >= HIGH_BORROWING_THRESHOLD) {
            this.portfolioCategory = PortfolioCategory.HIGH_SPENDING_HIGH_BORROWING;
        } else if (spendingRatio >= HIGH_SPENDING_THRESHOLD && totalAmountBorrowed < HIGH_BORROWING_THRESHOLD) {
            this.portfolioCategory = PortfolioCategory.HIGH_SPENDING_LOW_BORROWING;
        } else if (spendingRatio >= MODERATE_SPENDING_THRESHOLD && totalAmountBorrowed >= LOW_BORROWING_THRESHOLD) {
            this.portfolioCategory = PortfolioCategory.MODERATE_SPENDING_MODERATE_BORROWING;
        } else if (spendingRatio >= LOW_SPENDING_THRESHOLD && totalAmountBorrowed < LOW_BORROWING_THRESHOLD) {
            this.portfolioCategory = PortfolioCategory.LOW_SPENDING_LOW_BORROWING;
        } else if (spendingRatio < VERY_LOW_SPENDING_THRESHOLD && totalAmountBorrowed < LOW_BORROWING_THRESHOLD) {
            this.portfolioCategory = PortfolioCategory.VERY_LOW_SPENDING_MINIMAL_BORROWING;
        } else {
            this.portfolioCategory = PortfolioCategory.UNASSIGNED;
        }
    }



}
