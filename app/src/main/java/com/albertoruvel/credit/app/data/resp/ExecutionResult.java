package com.albertoruvel.credit.app.data.resp;

/**
 * Created by jose.rubalcaba on 10/27/2017.
 */

public class ExecutionResult {
    private String message;
    private boolean ok;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }
}
