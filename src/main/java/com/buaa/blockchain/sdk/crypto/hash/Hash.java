package com.buaa.blockchain.sdk.crypto.hash;

public interface Hash {
    String hash(final String inputData);

    String hashBytes(byte[] inputBytes);

    byte[] hash(final byte[] inputBytes);
}
