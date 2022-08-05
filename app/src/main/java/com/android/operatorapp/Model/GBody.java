package com.android.operatorapp.Model;

public class GBody<T> {
    private Boolean error;
    private String message;
    private T body;

    public GBody(Boolean error, String message, T body) {
        this.error = error;
        this.message = message;
        this.body = body;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
