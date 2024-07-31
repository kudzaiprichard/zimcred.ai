package com.intela.zimcredai.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    CUSTOMER_READ("customer:read"),
    CUSTOMER_UPDATE("customer:update"),
    CUSTOMER_CREATE("customer:create"),
    CUSTOMER_DELETE("customer:delete"),

    COORDINATOR_READ("coordinator:read"),
    COORDINATOR_UPDATE("coordinator:update"),
    COORDINATOR_CREATE("coordinator:create"),
    COORDINATOR_DELETE("coordinator:delete");

    @Getter
    private final String permission;

}
