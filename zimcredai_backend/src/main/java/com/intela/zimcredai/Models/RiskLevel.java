package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RiskLevel {
    LOW("low"), // Low risk, high likelihood of loan repayment
    MEDIUM("medium"), // Moderate risk, reasonable likelihood of loan repayment
    HIGH("high"), // High risk, low likelihood of loan repayment
    VERY_HIGH("very_high"), // Very high risk, very low likelihood of loan repayment
    UNASSIGNED("unassigned");
    @Getter
    private final String riskLevel;
}
