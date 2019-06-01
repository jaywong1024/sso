package com.sso.utils;

import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class DesCrypt {

//    默认的密钥，与客户端约定好的，应记录在物理存储中（数据库，本地文件）
    private static final String KEY = "huang-han-jie";
//    编码格式
    public static final String CODE_TYPE = "UTF-8";

    public static String encode(String key, String datasource) throws Exception {
        if (null == key) key = KEY;
//        随机生成器
        SecureRandom secureRandom = new SecureRandom();
//        创建 DES 秘钥
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(CODE_TYPE));




        return null;
    }


}
