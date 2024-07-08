package org.dananum.dananum_shop.global.web.enums;

public enum ProductOptionSize {
    XXL("XXL"),
    XL("XL"),
    L("L"),
    M("M"),
    S("S"),
    XS("XS"),
    XXS("XXS");
    private final String size;

    ProductOptionSize(String size) { this.size = size; }

    public String getCategory() { return size; }
}
