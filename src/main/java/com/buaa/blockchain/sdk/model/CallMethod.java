package com.buaa.blockchain.sdk.model;

public class CallMethod {
    private String method;
    private Object[] params;

    public CallMethod() {
    }

    public CallMethod(String method, Object[] params) {
        this.method = method;
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParams() {
        return params;
    }
}
