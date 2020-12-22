package com.buaa.blockchain.sdk;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.CryptoSuite;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
import com.buaa.blockchain.sdk.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RemoteCall {
    public static void main(String[] args) throws JsonProcessingException {
        // 1. 初始化公私钥对
        String address = "0xb4f1f4be27eea76bdaec8f2ec86340a67a167e36";
        String private_key = "78f18327d1cd40a6cfc20899233e1bd2843c4f76ccef66d0386351dfc0c142ab";
        CryptoSuite cs = new CryptoSuite(CryptoType.SM_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair(private_key);

        // 2. 构造交易对象
        // 2.1 基础数据
        String from = address;
        String to = "0xcd78b0552d6008a375b859f83be8fd481d44f951";
        BigInteger value = BigInteger.ZERO;
        // 2.2 合约调用参数
        List paramlist = new ArrayList<>();
        paramlist.add(0, "AddEle"); // 调用合约方法
        paramlist.add(1, "zhco");   // 调用合约方法（参数1）
        paramlist.add(2, 22);       // 调用合约方法（参数2）
        paramlist.add(3, true);     // 调用合约方法（参数3）
        String data = new ObjectMapper().writeValueAsString(paramlist);
        // 2.3 组装交易对象
        Transaction tx = new Transaction(from, to, value, data);

        // 3. 对交易对象进行签名
        SignatureResult res = cs.sign(cs.hash(tx.toString()), keyPair);
        tx.setSig(res.convertToString());

        // 4. 签名验证
        System.out.println(cs.verify(keyPair.getHexPublicKey(), cs.hash(tx.toString()), res.convertToString()));
    }
}
