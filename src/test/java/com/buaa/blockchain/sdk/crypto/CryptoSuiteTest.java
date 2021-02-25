package com.buaa.blockchain.sdk.crypto;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.signature.ECDSASignatureResult;
import com.buaa.blockchain.sdk.crypto.signature.SM2SignatureResult;
import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
import com.buaa.blockchain.sdk.crypto.utils.Hex;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CryptoSuiteTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

    @Test
    public void testCreateKeyPair() throws Exception {
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        // generate keyPair
        CryptoKeyPair keyPair = cryptoSuite.createKeyPair();
        System.out.println(keyPair.getAddress());
        System.out.println(keyPair.getHexPublicKey());
        System.out.println(keyPair.getHexPrivateKey());
    }

    @Test
    public void testReloadPrivatekey() throws Exception {
        String prikey = "78f18327d1cd40a6cfc20899233e1bd2843c4f76ccef66d0386351dfc0c142ab";
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair keyPair = cryptoSuite.createKeyPair(prikey);
        System.out.println(keyPair.getAddress());
        System.out.println(keyPair.getHexPublicKey());
    }

    @Test
    public void testECVerify() throws ECKey.SignatureException {
        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair();

        byte[] msgHash = cs.hash("123456".getBytes());
        ECDSASignatureResult res = (ECDSASignatureResult)cs.sign(msgHash, keyPair);

        byte[] r = res.getR();
        byte[] s = res.getS();
        byte v = res.getV();
        ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r, s, v);
        byte[] pubKeyBytes = ECKey.recoverPubBytesFromSignature(v, sig, msgHash);
        System.out.println(keyPair.getHexPublicKey());
        System.out.println(keyPair.getAddress());

        System.out.println(Hex.toHexString(res.getSignatureBytes()).length());
//        System.out.println(keyPair.getAddress(Hex.toHexString(pubKeyBytes).substring(2)));
//        Assert.assertEquals(keyPair.getHexPublicKey(), Hex.toHexString(pubKeyBytes));
//        Assert.assertEquals(keyPair.getAddress(), keyPair.getAddress(Hex.toHexString(pubKeyBytes)));
//        Assert.assertTrue(cs.verify(keyPair.getHexPublicKey(), msgHash, res.getSignatureBytes()));
    }

    @Test
    public void testSmVerify(){
        CryptoSuite cs = new CryptoSuite(CryptoType.SM_TYPE);
        CryptoKeyPair keyPair = cs.createKeyPair();

        byte[] msgHash = cs.hash("123456".getBytes());
        SignatureResult signatureResult = cs.sign(msgHash, keyPair);

        byte[] pubKeyBytes = ((SM2SignatureResult)signatureResult).getPub();

        Assert.assertEquals(keyPair.getHexPublicKey().substring(2), Hex.toHexString(pubKeyBytes));
        Assert.assertEquals(keyPair.getAddress(), keyPair.getAddress(Hex.toHexString(pubKeyBytes)));

        Assert.assertTrue(cs.verify(keyPair.getHexPublicKey(), msgHash, signatureResult.getSignatureBytes()));
        Assert.assertTrue(cs.verify("04"+Hex.toHexString(pubKeyBytes), msgHash, signatureResult.getSignatureBytes()));
    }

}
