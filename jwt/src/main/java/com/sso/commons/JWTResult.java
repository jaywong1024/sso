package com.sso.commons;

import io.jsonwebtoken.Claims;

/**
 * 结果对象
 */
public class JWTResult {

//    错误编码，在 JWTUtils 中定义常量，200 为正确
    private int errorCode;

//    是否成功
    private boolean success;

//    验证过程中 Payload 中的数据
    private Claims claims;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

}
