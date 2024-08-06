package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PortfolioClassification {
    EXCELLENT("excellent"), // Outstanding performance with minimal defaults
    GOOD("good"), // Strong performance with few minor issues
    AVERAGE("average"), // Moderate performance with some defaults and late repayments
    POOR("poor"),  // Significant performance issues with high defaults and frequent late repayments
    UNASSIGNED("unassigned");

    @Getter
    private final String portfolioClassification;
}
