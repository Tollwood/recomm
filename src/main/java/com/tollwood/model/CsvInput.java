package com.tollwood.model;

public class CsvInput {
    private String customerNumber;
    private boolean recommendationActive;
    private String rec1;
    private String rec2;
    private String rec3;
    private String rec4;
    private String rec5;
    private String rec6;
    private String rec7;
    private String rec8;
    private String rec9;
    private String rec10;

    public CsvInput() {
    }

    public CsvInput(String customerNumber, boolean recommendationActive, String rec1, String rec2, String rec3, String rec4, String rec5, String rec6, String rec7, String rec8, String rec9, String rec10) {
        this.customerNumber = customerNumber;
        this.recommendationActive = recommendationActive;
        this.rec1 = rec1;
        this.rec2 = rec2;
        this.rec3 = rec3;
        this.rec4 = rec4;
        this.rec5 = rec5;
        this.rec6 = rec6;
        this.rec7 = rec7;
        this.rec8 = rec8;
        this.rec9 = rec9;
        this.rec10 = rec10;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public boolean isRecommendationActive() {
        return recommendationActive;
    }

    public void setRecommendationActive(boolean recommendationActive) {
        this.recommendationActive = recommendationActive;
    }

    public String getRec1() {
        return rec1;
    }

    public void setRec1(String rec1) {
        this.rec1 = rec1;
    }

    public String getRec2() {
        return rec2;
    }

    public void setRec2(String rec2) {
        this.rec2 = rec2;
    }

    public String getRec3() {
        return rec3;
    }

    public void setRec3(String rec3) {
        this.rec3 = rec3;
    }

    public String getRec4() {
        return rec4;
    }

    public void setRec4(String rec4) {
        this.rec4 = rec4;
    }

    public String getRec5() {
        return rec5;
    }

    public void setRec5(String rec5) {
        this.rec5 = rec5;
    }

    public String getRec6() {
        return rec6;
    }

    public void setRec6(String rec6) {
        this.rec6 = rec6;
    }

    public String getRec7() {
        return rec7;
    }

    public void setRec7(String rec7) {
        this.rec7 = rec7;
    }

    public String getRec8() {
        return rec8;
    }

    public void setRec8(String rec8) {
        this.rec8 = rec8;
    }

    public String getRec9() {
        return rec9;
    }

    public void setRec9(String rec9) {
        this.rec9 = rec9;
    }

    public String getRec10() {
        return rec10;
    }

    public void setRec10(String rec10) {
        this.rec10 = rec10;
    }
}
