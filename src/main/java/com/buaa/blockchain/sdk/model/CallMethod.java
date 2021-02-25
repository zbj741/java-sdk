package com.buaa.blockchain.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
public class CallMethod {
    private String method;
    private Object[] params;

    public CallMethod() {
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", CallMethod.class.getSimpleName() + "[", "]")
                .add("method='" + method + "'")
                .add("params=" + Arrays.toString(params))
                .toString();
    }
}
