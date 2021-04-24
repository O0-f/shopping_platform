package org.example.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EncryptUtils {

    private static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    private static final int RSA_ENCRYPT_BLOCK = 117;
    private static final int RSA_DECRYPT_BLOCK = 128;
    private static final String SHA_ALGORITHM = "SHA-256";

    public static Map<String, String> generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        Map<String, String> map = new HashMap<>();
        map.put("PublicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        map.put("PrivateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return map;
    }

    public static String RSAEncrypt(String data, Key publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_PKCS1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = data.getBytes();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0, offset = 0; offset < bytes.length; i++, offset = i * RSA_ENCRYPT_BLOCK) {
            int length = RSA_ENCRYPT_BLOCK;
            if (length >= bytes.length - offset) {
                length = bytes.length - offset;
            }
            byteArrayOutputStream.write(cipher.doFinal(bytes, offset, length));
        }
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static String RSADecrypt(String data, Key privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_PKCS1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = Base64.getDecoder().decode(data);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0, offset = 0; offset < bytes.length; i++, offset = i * RSA_DECRYPT_BLOCK) {
            int length = i + RSA_DECRYPT_BLOCK;
            if (length >= bytes.length - offset) {
                length = bytes.length - offset;
            }
            byteArrayOutputStream.write(cipher.doFinal(bytes, offset, length));
        }
        return byteArrayOutputStream.toString();
    }

    public static Key getPublicKey(String publicKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(publicKey);
        return KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new X509EncodedKeySpec(bytes));
    }

    public static Key getPrivateKey(String privateKey) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(privateKey);
        return KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public static String SHAEncrypt(String data) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(SHA_ALGORITHM);
        return new String(messageDigest.digest(data.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    public static boolean SHACompare(String source, String destination) {
        return MessageDigest.isEqual(source.getBytes(StandardCharsets.UTF_8), destination.getBytes(StandardCharsets.UTF_8));
    }
}
