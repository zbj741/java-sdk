package com.buaa.blockchain.sdk.crypto.exceptions;

public class HashException extends RuntimeException {
    public HashException(String message) {
        super(message);
    }

    public HashException(String message, Throwable cause) {
        super(message, cause);
    }
}
