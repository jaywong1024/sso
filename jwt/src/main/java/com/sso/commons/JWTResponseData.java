package com.sso.commons;

/**
 * 返回给客户端的数据对象
 * 商业开发中，除了特殊请求之外，大多数响应数据都是用一个统一的数据类型
 * 统一数据可以有统一的处理方式，便于开发和维护
 */
public class JWTResponseData {

//    状态码
    private Integer code;

//    业务数据
    private Object data;

//    消息描述
    private String message;

//    身份令牌
    private String token;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
