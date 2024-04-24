package com.liam.sealed.utility;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionHelper {
    private static final String ALGORITHM = "AES";
    private static byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public static String encrypt(String valueToEnc) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128);
        SecretKey secretKey = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByteValue = cipher.doFinal(valueToEnc.getBytes("utf-8"));
        String encryptedValue64 = android.util.Base64.encodeToString(encryptedByteValue, android.util.Base64.DEFAULT);
        return encryptedValue64;
    }

    public static String decrypt(String encryptedValue) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128);
        SecretKey secretKey = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedValue64 = android.util.Base64.decode(encryptedValue, android.util.Base64.DEFAULT);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue, "utf-8");
        return decryptedValue;
    }
}