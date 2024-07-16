package org.dananum.dananum_shop.global.web.enums.inquiry;

public enum InquiryStatus {

    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String status;

    InquiryStatus(String status) {
        this.status = status;}

    public String getStatus() {return status;}
}
