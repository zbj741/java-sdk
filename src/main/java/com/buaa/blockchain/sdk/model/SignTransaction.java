package com.buaa.blockchain.sdk.model;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.CryptoSuite;
import com.buaa.blockchain.sdk.crypto.ECKey;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.keypair.ECDSAKeyPair;
import com.buaa.blockchain.sdk.crypto.keypair.SM2KeyPair;
import com.buaa.blockchain.sdk.crypto.rlp.RlpEncoder;
import com.buaa.blockchain.sdk.crypto.rlp.RlpList;
import com.buaa.blockchain.sdk.crypto.rlp.RlpString;
import com.buaa.blockchain.sdk.crypto.rlp.RlpType;
import com.buaa.blockchain.sdk.crypto.signature.*;
import com.buaa.blockchain.sdk.crypto.utils.Hex;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * xxxx
 *
 * @author <a href="http://github.com/hackdapp">hackdapp</a>
 * @date 2021/2/24
 * @since JDK1.8
 */
public class SignTransaction extends Transaction{
    private SignatureResult signatureResult;

    public SignTransaction(byte[] to, BigInteger value, byte[] data, Timestamp timestamp, SignatureResult signatureResult) {
        super(to, value, data, timestamp);
        this.signatureResult = signatureResult;
    }

    @JsonIgnore
    public String getFrom() throws IllegalAccessException {
        Signature signature = null;
        String publicKey = null;
        CryptoSuite cryptoSuite = null;
        CryptoKeyPair cryptoKeyPair = null;
        if(signatureResult instanceof ECDSASignatureResult){
            cryptoKeyPair = new ECDSAKeyPair();
            cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
            signature = new ECDSASignature();

            byte[] r = signatureResult.getR();
            byte[] s = signatureResult.getS();
            byte v = ((ECDSASignatureResult)signatureResult).getV();

            ECKey.ECDSASignature sig = ECKey.ECDSASignature.fromComponents(r, s, v);
            byte[] pubKeyBytes = ECKey.recoverPubBytesFromSignature(v-27, sig, cryptoSuite.hash(encode(this, null)));
            publicKey = Hex.toHexString(pubKeyBytes);
        }else if(signatureResult instanceof SM2SignatureResult){
            cryptoKeyPair = new SM2KeyPair();
            cryptoSuite = new CryptoSuite(CryptoType.SM_TYPE);
            signature = new SM2Signature();

            publicKey = "04"+ Hex.toHexString(((SM2SignatureResult) signatureResult).getPub());
        }
        if(!signature.verify(publicKey, cryptoSuite.hash(encode(this, null)), signatureResult.getSignatureBytes())){
            throw new IllegalAccessException();
        }
        return cryptoKeyPair.getAddress(publicKey.substring(2));
    }


    private byte[] encode(Transaction tx, SignatureResult signature) {
        List<RlpType> values = asRlpValues(tx, signature);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    private static List<RlpType> asRlpValues(Transaction tx, SignatureResult signatureResult) {
        List<RlpType> result = new ArrayList<>();
        // 1. 接收地址
        if (tx.getTo() != null) {
            result.add(RlpString.create(tx.getTo()));
        } else {
            result.add(RlpString.create(""));
        }
        // 2. 接收金额
        result.add(RlpString.create(tx.getValue()));
        // 3. 合约执行方法及参数
        if(tx.getData() != null){
            result.add(RlpString.create(tx.getData()));
        }else{
            result.add(RlpString.create(""));
        }
        // 4. 签名数据
        if (signatureResult != null) {
            result.addAll(signatureResult.encode());
        }
        return result;
    }

}
