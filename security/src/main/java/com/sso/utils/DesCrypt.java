package com.sso.utils;

import java.security.SecureRandom;

public class DesCrypt {

//    默认的密钥
    private static final String KEY = "huang-han-jie";
//    编码格式
    public static final String CODE_TYPE = "UTF-8";

    public static String encode(String key, String datasource) throws Exception {
        if (null == key) key = KEY;
//        随机生成器
        SecureRandom secureRandom = new SecureRandom();
        return null;
    }


}
