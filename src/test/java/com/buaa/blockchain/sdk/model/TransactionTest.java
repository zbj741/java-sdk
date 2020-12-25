package com.buaa.blockchain.sdk.model;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.CryptoSuite;
import com.buaa.blockchain.sdk.crypto.ECKey;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
import com.buaa.blockchain.sdk.crypto.utils.Hex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;


public class TransactionTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

    @Test
    public void testSigTransaction() throws Exception {
        CryptoSuite cs = new CryptoSuite(CryptoType.SM_TYPE);
        CryptoKeyPair from = cs.createKeyPair();
        CryptoKeyPair to = cs.createKeyPair();
        Transaction tx = new Transaction(from.getAddress().getBytes(), to.getAddress().getBytes(), BigInteger.ZERO, null);
        System.out.println(to.getAddress());
        SignatureResult res = cs.sign(cs.hash(tx.toString()), from);

        System.out.println(res.getSignatureBytes().length);
        System.out.println(cs.verify(from.getHexPublicKey(), cs.hash(tx.toString()), res.convertToString()));

        byte[] r = res.getR();
        byte[] s = res.getS();
        byte v = 27;
        byte[] address = ECKey.signatureToAddress(cs.hash(tx.toString().getBytes()), ECKey.ECDSASignature.fromComponents(r, s, v));
        System.out.println(from.getAddress());
        System.out.println(Hex.toHexString(address));
    }

    @Test
    public void testHexEncode(){
        String str = "this is a string";
        byte[] val = Hex.encode(str.getBytes());
        System.out.println(Hex.toHexString(val));

        byte[] decode = Hex.decode(val);
        System.out.println(new String(decode));

        String tmp1 = Hex.toHexString(str.getBytes());
        System.out.println(new String(Hex.decode(tmp1.getBytes())));
    }

}
