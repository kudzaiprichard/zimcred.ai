package com.intela.zimcredai.Models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RepaymentStatus {
    CURRENT("current"), //The borrower is up-to-date with all payments.
    LATE("late"), //The borrower has missed one or more payments but is still within a grace period.
    DELINQUENT("delinquent"), // The borrower is significantly behind on payments and may face penalties.
    DEFAULTED("defaulted"),// The borrower has failed to make payments for an extended period, and the loan is considered in default.
    PAID_OFF("paid_off"), // The loan has been fully repaid.
    DEFERRED("deferred"); //Payments have been temporarily paused, often due to special circumstances like economic hardship or further education.

    private final String repaymentStatus;

}
