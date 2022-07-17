package com.plugspace.model;

import java.io.Serializable;

public class SubscriptionsModel implements Serializable {
    String strSubName;
    String strDollarPrice;
    String strMonthlySub;
    boolean BooleanSelected;

    public SubscriptionsModel(String strSubName, String strDollarPrice, String strMonthlySub, boolean BooleanSelected) {
        this.strSubName = strSubName;
        this.strDollarPrice = strDollarPrice;
        this.strMonthlySub = strMonthlySub;
        this.BooleanSelected = BooleanSelected;
    }

    public String getStrSubName() {
        return strSubName;
    }

    public void setStrSubName(String strSubName) {
        this.strSubName = strSubName;
    }

    public String getStrDollarPrice() {
        return strDollarPrice;
    }

    public void setStrDollarPrice(String strDollarPrice) {
        this.strDollarPrice = strDollarPrice;
    }

    public String getStrMonthlySub() {
        return strMonthlySub;
    }

    public void setStrMonthlySub(String strMonthlySub) {
        this.strMonthlySub = strMonthlySub;
    }

    public boolean getBooleanSelected() {
        return BooleanSelected;
    }

    public void setBooleanSelected(boolean booleanSelected) {
        BooleanSelected = booleanSelected;
    }
}