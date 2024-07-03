package org.dananum.dananum_shop.global.web.enums;

public enum EmailCertificationState {

    NEEDED("NEEDED"),
    COMPLETED("COMPLETED"),
    ADMIN("ADMIN");

    private final String state;

    EmailCertificationState(String state) {
        this.state = state;}

    public String getType() {return state;}
}
