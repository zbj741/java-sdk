package com.buaa.blockchain.sdk;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.CryptoSuite;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.utils.Hex;
import com.buaa.blockchain.sdk.model.CallMethod;
import com.buaa.blockchain.sdk.model.SignTransaction;
import com.buaa.blockchain.sdk.model.Transaction;
import com.buaa.blockchain.sdk.util.HttpClientResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

/**
 * xxxx
 *
 * @author <a href="http://github.com/hackdapp">hackdapp</a>
 * @date 2021/2/24
 * @since JDK1.8
 */
public class ChainSDKSMTest {
    private int CRYPTO_TYPE = CryptoType.SM_TYPE;
    private ChainSDK chainSDK;
    private CryptoKeyPair cryptoKeyPair;
    private String privateKey = "152501dbf4fd94136678b9bbe34130b6eeda6373d108d5960afd686b77941126";

    @Before
    public void setUp() throws Exception {
        chainSDK = new ChainSDK("http://123.57.245.252:8080", CRYPTO_TYPE, privateKey);
        cryptoKeyPair = new CryptoSuite(CRYPTO_TYPE).createKeyPair(privateKey);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void deployContract() throws IOException, ClassNotFoundException {
        HttpClientResult result = chainSDK.deployContract("HelloWorldContract");
        System.out.println(result);
    }

    @Test
    public void call() throws JsonProcessingException {
       HttpClientResult result =  chainSDK.call("0x7f55da6b798c6202ca815a1f0af3aa2cf78f2206", "setName", new Object[]{ "zhang"});
       System.out.println(result);
    }

    @Test
    public void testDecodeTx() throws IOException, IllegalAccessException {
        CallMethod callMethod =  new CallMethod("setName", new Object[]{"zhang"});
        byte[] data = new ObjectMapper().writeValueAsBytes(callMethod);
        Transaction tx = new Transaction("0x7f55da6b798c6202ca815a1f0af3aa2cf78f2206".getBytes(), BigInteger.ZERO, data);
        String encodeStr = chainSDK.encodeAndSign(tx, cryptoKeyPair);

        SignTransaction dtx = (SignTransaction) chainSDK.decodeTx(encodeStr);

        Assert.assertEquals(Hex.toHexString(tx.getTo()), Hex.toHexString(dtx.getTo()));
        Assert.assertEquals(tx.getValue(), dtx.getValue());
        Assert.assertEquals(Hex.toHexString(tx.getData()), Hex.toHexString(dtx.getData()));
        Assert.assertEquals(cryptoKeyPair.getAddress(), dtx.getFrom());

        CallMethod decodeCallMethod = new ObjectMapper().readValue(dtx.getData(), CallMethod.class);
        Assert.assertEquals(callMethod.toString(), decodeCallMethod.toString());
    }


}