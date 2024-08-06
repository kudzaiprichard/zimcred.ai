package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PortfolioCategory {
    HIGH_SPENDING_HIGH_BORROWING("high_spending_high_borrowing"),
    HIGH_SPENDING_LOW_BORROWING("high_spending_low_borrowing"),
    MODERATE_SPENDING_MODERATE_BORROWING("moderate_spending_moderate_borrowing"),
    LOW_SPENDING_LOW_BORROWING("low_spending_low_borrowing"),
    VERY_LOW_SPENDING_MINIMAL_BORROWING("very_low_spending_minimal_borrowing"),
    UNASSIGNED("unassigned");

    @Getter
    private final String portfolioCategory;
}

