package org.dananum.dananum_shop.global.web.enums.user;

public enum AccountStatus {
    ACTIVE("ACTIVE"),
    DELETED("DELETED");
    private final String type;

    AccountStatus(String type) { this.type = type; }

    public String getType() { return type; }
}
