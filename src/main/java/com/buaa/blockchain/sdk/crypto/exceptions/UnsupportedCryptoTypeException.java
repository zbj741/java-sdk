package com.buaa.blockchain.sdk.crypto.exceptions;

public class UnsupportedCryptoTypeException extends RuntimeException {
    public UnsupportedCryptoTypeException(String message) {
        super(message);
    }

    public UnsupportedCryptoTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
