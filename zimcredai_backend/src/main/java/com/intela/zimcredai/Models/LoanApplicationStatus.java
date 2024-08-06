package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LoanApplicationStatus {

    APPROVED("pending"),
    SUSPENDED("suspended"),
    REJECTED("rejected"),
    PENDING("pending");

    @Getter
    private final String loanAmount;
}
