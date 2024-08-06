package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DocumentStatus {
    PENDING_REVIEW("pending_review"), // The document is awaiting review.
    APPROVED("approved"), // The document has been reviewed and approved.
    REJECTED("rejected"), // The document has been reviewed and rejected.
    REQUIRES_RESUBMISSION("requires_resubmission"), // The document needs to be resubmitted.
    EXPIRED("expired"); // The document is no longer valid.

    @Getter
    private final String documentStatus;

}
