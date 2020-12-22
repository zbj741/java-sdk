package com.buaa.blockchain.sdk.crypto;

import com.buaa.blockchain.sdk.config.CryptoType;
import com.buaa.blockchain.sdk.crypto.keypair.CryptoKeyPair;
import org.junit.After;
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

}
