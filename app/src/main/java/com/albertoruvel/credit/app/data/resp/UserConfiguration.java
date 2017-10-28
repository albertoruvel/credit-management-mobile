package com.albertoruvel.credit.app.data.resp;

/**
 * Created by jose.rubalcaba on 10/26/2017.
 */

public class UserConfiguration {
    private String name;
    private String email;
    private Double monthlyIncome;
    private boolean notificationEnabled;

    public UserConfiguration() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
}
