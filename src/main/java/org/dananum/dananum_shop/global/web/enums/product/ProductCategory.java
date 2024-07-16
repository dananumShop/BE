package org.dananum.dananum_shop.global.web.enums.product;

public enum ProductCategory {
    CLOTH("CLOTH"),

    BEDDING("BEDDING");
    private final String category;

    ProductCategory(String category) { this.category = category; }

    public String getCategory() { return category; }
}
