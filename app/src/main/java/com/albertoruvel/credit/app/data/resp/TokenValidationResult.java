package com.albertoruvel.credit.app.data.resp;

/**
 * Created by jose.rubalcaba on 10/26/2017.
 */

public class TokenValidationResult {
    private boolean valid;
    private String message;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
