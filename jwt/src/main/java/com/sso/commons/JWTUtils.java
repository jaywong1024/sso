package com.sso.commons;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

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
     * 生成秘钥
     * @return 秘钥
     */
    public static SecretKey generateKey() {
        try {
            byte[] encodedKey = JWT_SECRET.getBytes("UTF-8");
            SecretKey secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            return secretKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 签发 JWT，创建 Token 的方法
     * 此方法创建的 token 是一次性的，是为一个用户的有效登录周期准备的一个 token，当用户退出或超时，token 失效
     * @param id jwt 唯一身份标识，避免一次性的 token数据 被用于重放攻击
     * @param issuer jwt 签发者
     * @param subject jwt 所面向的用户，Payload 中携带的 public claims
     * @param expirationTime jwt 有效时间，单位毫秒
     * @return token
     */
    public static String createJWT(String id, String issuer, String subject, long expirationTime) {
//        加密算法 HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//        当前时间，单位毫秒
        long nowMillis = System.currentTimeMillis();
//        获取通用秘钥
        SecretKey secretKey = generateKey();
//        创建 jwt 构建器
        JwtBuilder jwtBuilder = Jwts.builder()
//                身份标识（客户端的唯一标记），如：用户的主键、客户端 ip地址、服务器生成的随机数据等
                .setId(id)
//                签发者
                .setIssuer(issuer)
//                受众用户
                .setSubject(subject)
//                发行时间
                .setIssuedAt(new Date(nowMillis))
//                秘钥和算法
                .signWith(signatureAlgorithm, secretKey);

//            失效时间
        if (expirationTime > 0) {
            expirationTime += nowMillis;
            jwtBuilder.setExpiration(new Date(expirationTime));
        }
//        生成 token
        return jwtBuilder.compact();
    }

    /**
     * 校验 jwt
     * @param jwt 服务端为一个客户端登录请求时生成的 token，当客户端再次发送请求的时候必须携带此 token
     * @return checkResult 校验结果
     */
    public static JWTResult validateJWT(String jwt) {
        JWTResult checkResult = new JWTResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwt);
//            没有错误信息代表校验后才能给
            checkResult.setSuccess(true);
//            被校验的 jwt 中的 Payload 数据
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
//            token 超时
            checkResult.setErrorCode(JWT_ERRORCODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
//            token 签名失效，校验失败
            checkResult.setErrorCode(JWT_ERRORCODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
//            其他错误
            checkResult.setErrorCode(JWT_ERRORCODE_FAIL);
            checkResult.setSuccess(false);
            e.printStackTrace();
        }
        return checkResult;
    }

    /**
     * 解析 jwt
     * @param jwt 需要解析的 jwt
     * @return Payload 中保存的所有 claims
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
//        获取通用秘钥
        SecretKey secretKey = generateKey();
//        Jwts.parser() 返回一个专门用于解析 token 的对象
        return Jwts.parser()
//                解析时使用的秘钥
                .setSigningKey(secretKey)
//                解析的 jwt
                .parseClaimsJws(jwt)
//                getBody() 获取 jwt 中记录的 Payload，就是 Payload 中保存的所有 claims(id, iss, sub, iat, exp, 其他数据等)
                .getBody();
    }

    /**
     * 转换生成 Payload 中的公开数据 public claims
     * @param object 要转换成 Subject 的对象，一般是 实体类型，或 BO、DTO 等数据对象
     * @return java对象 -> JSON对象 出错时返回 null
     */
    public static String generateSubject(Object object) {
        try {
//            生成 JSON 对象
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
