package com.sso.commons;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * JWT 工具
 */
public class JWTUtils {

//    服务器用户加密和解密的秘钥
    private static final String JWT_SECRET = "test_jwt_secret";

//    ObjectMapper类 是 Jackson库 的主要类，它提供一些功能可以将转 Java对象 和 JSON结构 相互转换
    private static final ObjectMapper MAPPER = new ObjectMapper();

//    Token 过期
    private static final int JWT_ERRORCODE_EXPIRE = 1005;

//    验证不通过
    private static final int JWT_ERRORCODE_FAIL = 1006;

    /**
     * 通用秘钥
     * @return
     */
    public static SecretKey generalKey() {
        try {
            byte[] encodedKey = JWT_SECRET.getBytes("UTF-8");
            SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            return secretKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
