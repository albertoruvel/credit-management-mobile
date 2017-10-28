package com.albertoruvel.credit.app.data.resp;

/**
 * Created by jose.rubalcaba on 10/25/2017.
 */

public class AuthenticationResult {
    private boolean success;
    private String message;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
