package org.dananum.dananum_shop.global.web.enums;

public enum ProductGender {
    MALE("MALE"),
    FEMALE("FEMALE");
    private final String gender;

    ProductGender(String gender) { this.gender = gender; }

    public String getGender() { return gender; }
}
