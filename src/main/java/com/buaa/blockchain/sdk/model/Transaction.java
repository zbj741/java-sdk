package com.buaa.blockchain.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private byte[] to;
    private BigInteger value;
    private byte[] data;
    private Timestamp timestamp;
}
