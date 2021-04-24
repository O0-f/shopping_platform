package org.example;

import org.example.utils.EncryptUtils;
import org.junit.Test;

import java.util.Map;

public class EncryptTest {

    private Map<String, String> map;
    private String source;
    private String destination;

    public EncryptTest() {
        try {
            map  = EncryptUtils.generateKeyPair();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void RSAEncrypt() {
        try {
            source = "Shopping_Platform_Test";
            destination = EncryptUtils.RSAEncrypt(source, EncryptUtils.getPublicKey(map.get("PublicKey")));
            System.out.println(EncryptUtils.RSADecrypt(destination, EncryptUtils.getPrivateKey(map.get("PrivateKey"))));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void SHAEncrypt() {
        try {
            source = "王逸飞";
            destination = "王逸飞";
            System.out.println(EncryptUtils.SHACompare(EncryptUtils.SHAEncrypt(source), EncryptUtils.SHAEncrypt(destination)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
