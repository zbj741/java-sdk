package com.buaa.blockchain.sdk.crypto;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.exceptions.UnsupportedCryptoTypeException;
import com.buaa.blockchain.sdk.crypto.hash.Hash;
import com.buaa.blockchain.sdk.crypto.hash.Keccak256;
import com.buaa.blockchain.sdk.crypto.hash.SM3Hash;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import com.buaa.blockchain.sdk.crypto.keypair.ECDSAKeyPair;
import com.buaa.blockchain.sdk.crypto.keypair.SM2KeyPair;
import com.buaa.blockchain.sdk.crypto.signature.ECDSASignature;
import com.buaa.blockchain.sdk.crypto.signature.SM2Signature;
import com.buaa.blockchain.sdk.crypto.signature.Signature;
import com.buaa.blockchain.sdk.crypto.signature.SignatureResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;

public class CryptoSuite {

    private static Logger logger = LoggerFactory.getLogger(CryptoSuite.class);

    public final int cryptoTypeConfig;

    public final Signature signatureImpl;
    public final Hash hashImpl;
    private final CryptoKeyPair keyPairFactory;
    private CryptoKeyPair cryptoKeyPair;


    /**
     * init the common crypto implementation according to the crypto type
     *
     * @param cryptoTypeConfig the crypto type config number
     */
    public CryptoSuite(int cryptoTypeConfig) {
        this.cryptoTypeConfig = cryptoTypeConfig;
        if (this.cryptoTypeConfig == CryptoType.ECDSA_TYPE) {
            this.signatureImpl = new ECDSASignature();
            this.hashImpl = new Keccak256();
            this.keyPairFactory = new ECDSAKeyPair();
        } else if (this.cryptoTypeConfig == CryptoType.SM_TYPE) {
            this.signatureImpl = new SM2Signature();
            this.hashImpl = new SM3Hash();
            this.keyPairFactory = new SM2KeyPair();
        } else {
            throw new UnsupportedCryptoTypeException(
                    "only support "
                            + CryptoType.ECDSA_TYPE
                            + "/"
                            + CryptoType.SM_TYPE
                            + " crypto type");
        }
    }

    public int getCryptoTypeConfig() {
        return cryptoTypeConfig;
    }

    public Signature getSignatureImpl() {
        return signatureImpl;
    }

    public Hash getHashImpl() {
        return hashImpl;
    }

    public String hash(final String inputData) {
        return hashImpl.hash(inputData);
    }

    public byte[] hash(final byte[] inputBytes) {
        return hashImpl.hash(inputBytes);
    }

    public SignatureResult sign(final byte[] message, final CryptoKeyPair keyPair) {
        return signatureImpl.sign(message, keyPair);
    }

    public SignatureResult sign(final String message, final CryptoKeyPair keyPair) {
        return signatureImpl.sign(message, keyPair);
    }

    public boolean verify(final String publicKey, final String message, final String signature) {
        return signatureImpl.verify(publicKey, message, signature);
    }

    public boolean verify(final String publicKey, final byte[] message, final byte[] signature) {
        return signatureImpl.verify(publicKey, message, signature);
    }

    public CryptoKeyPair createKeyPair() {
        this.cryptoKeyPair = this.keyPairFactory.generateKeyPair();
        return this.cryptoKeyPair;
    }

    public CryptoKeyPair createKeyPair(KeyPair keyPair) {
        this.cryptoKeyPair = this.keyPairFactory.createKeyPair(keyPair);
        return this.cryptoKeyPair;
    }

    public CryptoKeyPair createKeyPair(String hexedPrivateKey) {
        this.cryptoKeyPair = this.keyPairFactory.createKeyPair(hexedPrivateKey);
        return this.cryptoKeyPair;
    }

    public CryptoKeyPair getCryptoKeyPair() {
        return this.cryptoKeyPair;
    }

    public CryptoKeyPair getKeyPairFactory() {
        return this.keyPairFactory;
    }
}
