package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SupportRequestStatus {
    OPEN("open"), // The support request has been submitted and is awaiting a response.
    IN_PROGRESS("in_progress"), // The support request is currently being reviewed or worked on.
    RESOLVED("resolved"), // The support request has been addressed and resolved.
    CLOSED("closed"), // The support request has been closed.
    REOPENED("reopened"), // The support request has been reopened after being previously closed or resolved.
    ESCALATED("escalated"), // The support request has been escalated to a higher level of support.
    PENDING_CUSTOMER("pending_customer"); // The support team is waiting for additional information or action from the customer.

    @Getter
    private final String supportRequestStatus;

}
