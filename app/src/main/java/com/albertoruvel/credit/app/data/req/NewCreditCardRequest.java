package com.albertoruvel.credit.app.data.req;

/**
 * Created by jose.rubalcaba on 10/27/2017.
 */

public class NewCreditCardRequest {
    private String name;
    private Double annualFee;
    private Double maxSalary;
    private String periodDate;
    private String payDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(Double annualFee) {
        this.annualFee = annualFee;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getPeriodDate() {
        return periodDate;
    }

    public void setPeriodDate(String periodDate) {
        this.periodDate = periodDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}
