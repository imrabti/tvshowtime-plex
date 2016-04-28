package org.nuvola.tvshowtime.business.tvshowtime;

import java.io.Serializable;

public class Message implements Serializable {
    private String result;
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
