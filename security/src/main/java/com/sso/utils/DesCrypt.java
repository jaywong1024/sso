package com.sso.utils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.util.Locale;

public class DesCrypt {

//    与客户端对应的密钥，一般存放在数据库或本地文件中
    private static final String KEY = "huang-han-jie";

    private static final String DES_ALGORITHM = "DES";

    /**
     * 获取 Cipher 对象解密加密时使用的密钥
     * @param key 密钥源
     * @return 加密和解密使用密钥
     */
    private static SecretKey generateKey(String key) {
        try {
//            获取 DES 密钥工厂
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
//            获取解密加密使用的密钥
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            return secretKeyFactory.generateSecret(desKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 16进制字符串转化为2进制数组
     * @param src 数据源
     * @return 2进制数组
     */
    private static byte[] hexStr2Bytes(String src) {
//        对输入值进行规范化整理
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
//        处理值初始化
        int m = 0, n = 0;
//        计算长度
        int iLen = src.length() / 2;
//        分配存储空间
        byte[] ret = new byte[iLen];

        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0X" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }

    /**
     * 2进制数组转16进制字符串
     * @param src 数据源
     * @return 16进制字符串
     */
    public static String Bytes2HexStr(byte src[]) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < src.length; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            strBuf.append(hex.toUpperCase());
        }
        return strBuf.toString();
    }


    /**
     * 解密
     * @param key 密钥
     * @param ciphertext 密文（16进制）
     * @return 明文字符串
     */
    public static String decrypt(String key, String ciphertext) {
        if (null == key) key = KEY;
//        获取密钥
        SecretKey secretKey = generateKey(key);
        if (null == secretKey) return null;
//        创建加密解密对象 Cipher
        Cipher cipher = null;
        try {
//            解密对象初始化
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
//            返回解密数据字符串
            return new String(cipher.doFinal(hexStr2Bytes(ciphertext)), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param key 密钥
     * @param plaintext 明文
     * @return 密文（16进制字符串）
     */
    public static String encrypt(String key, String plaintext) {
        if (null == key) key = KEY;
//        获取密钥
        SecretKey secretKey = generateKey(key);
        if (null == secretKey) return null;
//        创建加密解密对象 Cipher
        Cipher cipher = null;
        try {
//            加密对象初始化
            cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            返回加密数据 16进制
            return Bytes2HexStr(cipher.doFinal(plaintext.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
