package com.buaa.blockchain.sdk;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.CryptoSuite;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.rlp.*;
import com.buaa.blockchain.sdk.crypto.signature.ECDSASignatureResult;
import com.buaa.blockchain.sdk.crypto.signature.SM2SignatureResult;
import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
import com.buaa.blockchain.sdk.crypto.utils.Numeric;
import com.buaa.blockchain.sdk.model.CallMethod;
import com.buaa.blockchain.sdk.model.SignTransaction;
import com.buaa.blockchain.sdk.model.Transaction;
import com.buaa.blockchain.sdk.util.HttpClientResult;
import com.buaa.blockchain.sdk.util.HttpClientUtils;
import com.buaa.blockchain.sdk.util.PackageUtil;
import com.buaa.blockchain.sdk.util.ReflectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ObjectArrays;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * xxxx
 *
 * @author <a href="http://github.com/hackdapp">hackdapp</a>
 * @date 2021/2/24
 * @since JDK1.8
 */
@Slf4j
public class ChainSDK {

    private String chainHost;
    private CryptoSuite cryptoSuite;
    private CryptoKeyPair cryptoKeyPair;
    private int cryptoType;

    public ChainSDK(int cryptoType){
        this.cryptoType = cryptoType;
    }

    public ChainSDK(String chainHost, int cryptoType, String privateKey) {
        this.chainHost = chainHost;
        this.cryptoType = cryptoType;
        this.cryptoSuite = new CryptoSuite(cryptoType);
        this.cryptoKeyPair = cryptoSuite.createKeyPair(privateKey);
    }

    public HttpClientResult deployContract(String contractName) throws IOException, ClassNotFoundException {
        InputStream in = ChainSDK.class.getResourceAsStream("/"+contractName+".class");
        byte[] byteData = ByteStreams.toByteArray(in);

        Class mainClass = ReflectUtil.getInstance().loadClass(contractName, byteData);
        Class[] classes = ObjectArrays.concat(mainClass, mainClass.getDeclaredClasses());
        byte[] bytes = PackageUtil.pack(classes);

        return sendSignTransaction(null, BigInteger.ZERO, bytes);
    }

    public HttpClientResult call(String contractAddr, String method, Object[] params) throws JsonProcessingException {
        CallMethod callMethod = new CallMethod(method, params);
        byte[] data = new ObjectMapper().writeValueAsBytes(callMethod);
        return sendSignTransaction(contractAddr, BigInteger.ZERO, data);
    }

    private HttpClientResult sendSignTransaction(String to, BigInteger val, byte[] data) {
        byte[] toAddr = to !=null ? to.getBytes() : null;
        Transaction tx = new Transaction(toAddr, val, data);
        try {
            String encodeData = encodeAndSign(tx, cryptoKeyPair);
            return HttpClientUtils.doPost(chainHost+"/api/v1/tx/send", encodeData);
        } catch (Exception ex){
            log.error("invoke sendSignTransaction", ex);
            return null;
        }
    }

    public String encodeAndSign(Transaction rawTransaction, CryptoKeyPair cryptoKeyPair) {
        return Numeric.toHexString(encodeAndSignBytes(rawTransaction, cryptoKeyPair));
    }

    public byte[] encodeAndSignBytes(Transaction rawTransaction, CryptoKeyPair cryptoKeyPair) {
        byte[] encodedTransaction = encodeTx(rawTransaction, null);
        byte[] hash = cryptoSuite.hash(encodedTransaction);
        SignatureResult result = cryptoSuite.sign(hash, cryptoKeyPair);
        return encodeTx(rawTransaction, result);
    }

    private byte[] encodeTx(Transaction tx, SignatureResult signature) {
        List<RlpType> values = asRlpValues(tx, signature);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    public Transaction decodeTx(String hexData){
        byte[] byteData = Numeric.hexStringToByteArray(hexData);
        RlpList rlpList = RlpDecoder.decode(byteData);

        RlpList values = (RlpList) rlpList.getValues().get(0);
        byte[] to = ((RlpString) values.getValues().get(0)).getBytes();
        BigInteger val = ((RlpString) values.getValues().get(1)).asPositiveBigInteger();
        byte[] data = ((RlpString) values.getValues().get(2)).getBytes();
        if(values.getValues().size()>3){
            SignatureResult signatureResult = null;
            if(cryptoType == CryptoType.ECDSA_TYPE){
                byte v = ((RlpString) values.getValues().get(3)).getBytes()[0];
                byte[] r = ((RlpString) values.getValues().get(4)).getBytes();
                byte[] s = ((RlpString) values.getValues().get(5)).getBytes();
                byte[] sig = ((RlpString) values.getValues().get(6)).getBytes();
                signatureResult = new ECDSASignatureResult(v, r, s, sig);
            } else if(cryptoType == CryptoType.SM_TYPE) {
                byte[] pubKeyBytes = ((RlpString) values.getValues().get(3)).getBytes();
                byte[] r = Numeric.toBytesPadded(Numeric.toBigInt(((RlpString) values.getValues().get(4)).getBytes()), 32);
                byte[] s = Numeric.toBytesPadded(Numeric.toBigInt(((RlpString) values.getValues().get(5)).getBytes()), 32);
                byte[] sig = ((RlpString) values.getValues().get(6)).getBytes();
                signatureResult = new SM2SignatureResult(pubKeyBytes, r, s, sig);
            }
            return new SignTransaction(to, val, data, signatureResult);
        } else {
           return new Transaction(to, val, data);
        }
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
