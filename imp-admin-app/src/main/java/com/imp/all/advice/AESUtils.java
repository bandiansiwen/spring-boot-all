package com.imp.all.advice;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author Longlin
 * @date 2022/10/18 17:06
 * @description
 */
public class AESUtils {

    // 秘钥
    private static final String aesKey = "1111111111111111";
    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 128位的AESkey
     */
    private static final byte[] AES_KEY = AESUtils.aesKey.getBytes(StandardCharsets.US_ASCII);

    // 获取 cipher
    private static Cipher getCipher(int model) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AES_KEY, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(model, secretKeySpec);
        return cipher;
    }

    // AES加密
    public static byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        return cipher.doFinal(data);
    }

    // AES解密
    public static byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }
}
