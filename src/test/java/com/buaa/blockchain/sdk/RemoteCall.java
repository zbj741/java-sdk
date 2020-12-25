package com.buaa.blockchain.sdk;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.CryptoSuite;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
import com.buaa.blockchain.sdk.model.CallMethod;
import com.buaa.blockchain.sdk.model.Transaction;
import com.buaa.blockchain.sdk.util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class RemoteCall {
    @Test
    public void testVerify() throws JsonProcessingException {
        // 1. 初始化公私钥对
        String address = "0xb4f1f4be27eea76bdaec8f2ec86340a67a167e36";
        String private_key = "78f18327d1cd40a6cfc20899233e1bd2843c4f76ccef66d0386351dfc0c142ab";
        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair(private_key);

        // 2. 构造交易对象
        // 2.1 基础数据
        String from = address;
        String to = "0xcd78b0552d6008a375b859f83be8fd481d44f951";
        BigInteger value = BigInteger.ZERO;
        // 2.2 合约调用参数
        CallMethod callMethod = new CallMethod("insertUser", new Object[]{"1", "zhang"});
        byte[] data = new ObjectMapper().writeValueAsBytes(callMethod);

        // 2.3 组装交易对象
        Transaction tx = new Transaction(from.getBytes(), to.getBytes(), value, data);

        // 3. 对交易对象进行签名
        SignatureResult res = cs.sign(cs.hash(tx.toString()), keyPair);

        // 4. 签名验证
        System.out.println(keyPair.getHexPublicKey());
        Assert.assertEquals(true, cs.verify(keyPair.getHexPublicKey(), cs.hash(tx.toString()), res.convertToString()));
    }

    @Test
    public void testTransfer() throws Exception {
        // 1. 初始化公私钥对
        String address = "0xd3e1f60edf3bf04e10d631ce3e7b99d81fcea00e";
        String private_key = "152501dbf4fd94136678b9bbe34130b6eeda6373d108d5960afd686b77941126";
        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair(private_key);

        // 2. 构造交易对象
        // 2.1 基础数据
        String from = address;
        String to = "0xcd78b0552d6008a375b859f83be8fd481d44f951";
        BigInteger value = BigInteger.valueOf(100l);

        // 2.3 组装交易对象
        Transaction tx = new Transaction(from.getBytes(), to.getBytes(), value, null);

        final String hash = cs.hash(tx.toString());
        System.out.println("hash:>"+hash);
        SignatureResult res = cs.sign(hash, keyPair);

        String txJson = new ObjectMapper().writeValueAsString(tx);
        HttpClientResult result = HttpClientUtils.doPost("http://127.0.0.1:17600/api/v1/tx/send?sig="+res.convertToString(), txJson);
        System.out.println(result);
    }

    @Test
    public void testDeployContract() throws Exception {
        // 1. 初始化公私钥对
        String address = "0xb4f1f4be27eea76bdaec8f2ec86340a67a167e36";
        String private_key = "78f18327d1cd40a6cfc20899233e1bd2843c4f76ccef66d0386351dfc0c142ab";
        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair(private_key);

        // 2. 构造交易对象
        // 2.1 基础数据
        String from = address;
        String to = null;
        BigInteger value = BigInteger.ZERO;
        // 2.2 合约调用参数
        String contractName = "DemoUserContract";
        String hexCode = "cafebabe0000003400280a0006001b090005001c0b001d001e0b001d001f0700200700210100036d617001000f4c6a6176612f7574696c2f4d61703b0100063c696e69743e010012284c6a6176612f7574696c2f4d61703b2956010004436f646501000f4c696e654e756d6265725461626c650100124c6f63616c5661726961626c655461626c65010004746869730100124c44656d6f55736572436f6e74726163743b0100104d6574686f64506172616d657465727301000a696e7365727455736572010027284c6a6176612f6c616e672f537472696e673b4c6a6176612f6c616e672f537472696e673b295601000269640100124c6a6176612f6c616e672f537472696e673b0100046e616d6501000a7570646174655573657201000764656c55736572010015284c6a6176612f6c616e672f537472696e673b295601000a536f7572636546696c6501001544656d6f55736572436f6e74726163742e6a6176610c000900220c000700080700230c002400250c0026002701001044656d6f55736572436f6e74726163740100106a6176612f6c616e672f4f626a65637401000328295601000d6a6176612f7574696c2f4d6170010003707574010038284c6a6176612f6c616e672f4f626a6563743b4c6a6176612f6c616e672f4f626a6563743b294c6a6176612f6c616e672f4f626a6563743b01000672656d6f7665010026284c6a6176612f6c616e672f4f626a6563743b294c6a6176612f6c616e672f4f626a6563743b002100050006000000010000000700080000000400010009000a0002000b00000046000200020000000a2ab700012a2bb50002b100000002000c0000000e00030000000e0004000f00090010000d0000001600020000000a000e000f00000000000a00070008000100100000000501000700000001001100120002000b0000004f000300030000000d2ab400022b2cb90003030057b100000002000c0000000a000200000013000c0014000d0000002000030000000d000e000f00000000000d0013001400010000000d0015001400020010000000090200130000001500000001001600120002000b0000004f000300030000000d2ab400022b2cb90003030057b100000002000c0000000a000200000017000c0018000d0000002000030000000d000e000f00000000000d0013001400010000000d0015001400020010000000090200130000001500000001001700180002000b00000044000200020000000c2ab400022bb90004020057b100000002000c0000000a00020000001b000b001c000d0000001600020000000c000e000f00000000000c00130014000100100000000501001300000001001900000002001a";

        byte[] data = ByteArrayUtil.merge(DataWord.of(contractName.getBytes()).getData(), HexUtil.fromHexString(hexCode));

        // 2.3 组装交易对象
        Transaction tx = new Transaction(from.getBytes(), null, value, data);

        final String hash = cs.hash(tx.toString());
        System.out.println("hash:>"+hash);
        SignatureResult res = cs.sign(hash, keyPair);

        String txJson = new ObjectMapper().writeValueAsString(tx);
        HttpClientResult result = HttpClientUtils.doPost("http://127.0.0.1:17600/api/v1/tx/send?sig="+res.convertToString(), txJson);
        System.out.println(result);
    }
    @Test
    public void testSendTx() throws Exception {
        // 1. 加载本地用户公私钥对
        String address = "0xb4f1f4be27eea76bdaec8f2ec86340a67a167e36";
        String private_key = "78f18327d1cd40a6cfc20899233e1bd2843c4f76ccef66d0386351dfc0c142ab";
        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair(private_key);

        // 2. 构造交易对象
        // 2.1 基础数据
        String from = address; //交易发起人
        String to = "0xcd78b0552d6008a375b859f83be8fd481d44f951"; //合约地址
        BigInteger value = BigInteger.ZERO; //转帐金额
        // 2.2 合约调用参数(合约方法，合约方法参数)
        CallMethod callMethod = new CallMethod("insertUser", new Object[]{"1", "zhang"});
        byte[] data = new ObjectMapper().writeValueAsBytes(callMethod);

        // 2.3 组装交易对象
        Transaction tx = new Transaction(from.getBytes(), to.getBytes(), value, data);

        // 3. 交易签名
        final String hash = cs.hash(tx.toString());
        SignatureResult res = cs.sign(hash, keyPair);
        String sig = res.convertToString();

        // 4. 发送请求至链平台
        String txJson = new ObjectMapper().writeValueAsString(tx);
        HttpClientResult result = HttpClientUtils.doPost("http://127.0.0.1:17600/api/v1/tx/send?sig="+sig, txJson);
        System.out.println(result);
    }
}
