package com.buaa.blockchain.sdk.model;

import java.math.BigInteger;
import java.util.Arrays;

public class Transaction {
    private byte[] from;
    private byte[] to;
    private BigInteger value;
    private byte[] data;

    public Transaction(byte[] from, byte[] to, BigInteger value, byte[] data) {
        this.from = from;
        this.to = to;
        this.value = value;
        this.data = data;
    }

    public byte[] getFrom() {
        return from;
    }

    public void setFrom(byte[] from) {
        this.from = from;
    }

    public byte[] getTo() {
        return to;
    }

    public void setTo(byte[] to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("data=").append(Arrays.toString(data));
        sb.append(", from=").append(Arrays.toString(from));
        sb.append(", to=").append(Arrays.toString(to));
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
