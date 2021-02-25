//package com.buaa.blockchain.sdk.model;
//
//import com.buaa.blockchain.sdk.config.CryptoType;
//import com.buaa.blockchain.sdk.crypto.CryptoSuite;
//import com.buaa.blockchain.sdk.crypto.ECKey;
//import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
//import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
//import com.buaa.blockchain.sdk.crypto.utils.Hex;
//import com.buaa.blockchain.sdk.util.HexUtil;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.math.BigInteger;
//
//
//public class TransactionTest {
//
//    @Before
//    public void before() throws Exception {
//    }
//
//    @After
//    public void after() throws Exception {
//    }
//
//    @Test
//    public void testSigMessage() throws ECKey.SignatureException {
//        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
//        CryptoKeyPair keyPair = cs.createKeyPair();
//
//        byte[] messageHash = cs.hash("123456".getBytes());
//        SignatureResult res = cs.sign(messageHash, keyPair);
//
//        byte[] r = res.getR();
//        byte[] s = res.getS();
//        byte v = (byte) 0x1b;
//        ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r, s, v);
//
//        byte[] pubKeyBytes = ECKey.recoverPubBytesFromSignature(0, sig, messageHash);
//        System.out.println(HexUtil.toHexString(pubKeyBytes));
//        System.out.println(keyPair.getHexPublicKey());
//
//        byte[] addBytes = ECKey.signatureToAddress(messageHash, sig);
//        System.out.println(keyPair.getAddress());
//        System.out.println(HexUtil.toHexString(addBytes));
//    }
//
//    @Test
//    public void testSigTransaction() throws Exception {
//        CryptoSuite cs = new CryptoSuite(CryptoType.ECDSA_TYPE);
//        CryptoKeyPair from = cs.createKeyPair();
//        System.out.println(from.getAddress());
//        System.out.println(from.getHexPublicKey());
//
//        Transaction tx = new Transaction(from.getAddress().getBytes(), "0x2cf4b8b6e7d9db070100a163bd0bc5b281b6f5fc".getBytes(), BigInteger.ZERO, null);
//        final byte[] txHash = cs.hash(tx.toString().getBytes());
//        SignatureResult res = cs.sign(txHash, from);
//
//        byte[] r = res.getR();
//        byte[] s = res.getS();
//        byte v = (byte) 0x1b;
//        ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r, s, v);
//        byte[] pubKeyBytes = ECKey.recoverPubBytesFromSignature(0, sig, txHash);
//        System.out.println(HexUtil.toHexString(pubKeyBytes));
//
//        System.out.println(cs.verify(from.getHexPublicKey(), txHash, res.getSignatureBytes()));
//    }
//
//
//    @Test
//    public void testHexEncode(){
//        String str = "this is a string";
//        byte[] val = Hex.encode(str.getBytes());
//        System.out.println(Hex.toHexString(val));
//
//        byte[] decode = Hex.decode(val);
//        System.out.println(new String(decode));
//
//        String tmp1 = Hex.toHexString(str.getBytes());
//        System.out.println(new String(Hex.decode(tmp1.getBytes())));
//    }
//
//}
