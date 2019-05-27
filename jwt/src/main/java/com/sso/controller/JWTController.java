package com.sso.controller;

import com.sso.commons.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class JWTController {

    /**
     * 验证客户端登录，如果用户名和密码正确则创建一个 Token令牌返回给客户端，客户端发送请求时必需携带此 Token
     * @param username 账号
     * @param password 密码
     * @return jwtResponseData(状态码，业务数据，描述信息，Token令牌)
     */
    @ResponseBody
    @PostMapping(value = "/login")
    public Object login(String username, String password) {
        System.out.println("JWTController.login: " + username + ", " + password);
        JWTResponseData jwtResponseData = null;
//        校验账号密码，这里使用的是 JWTUsers 静态数据来模拟数据库
        if (JWTUsers.isLogin(username, password)) {
            JWTSubject jwtSubject = new JWTSubject(username);
//            创建 token 令牌：使用 UUID 为一个客户端提供一个唯一标记，签发者 cp-test-jwt，JSON格式 的主题，有效时长 60s
            String jwtToken = JWTUtils.createJWT(UUID.randomUUID().toString(),
                    "cp-test-jwt", JWTUtils.generateSubject(jwtSubject), (1 * 60 * 1000));
            jwtResponseData = new JWTResponseData();
            jwtResponseData.setCode(200);
            jwtResponseData.setData(null);
            jwtResponseData.setMessage("登陆成功");
            jwtResponseData.setToken(jwtToken);
        } else {
            jwtResponseData = new JWTResponseData();
            jwtResponseData.setCode(500);
            jwtResponseData.setData(null);
            jwtResponseData.setMessage("登录失败");
            jwtResponseData.setToken(null);
        }
        return jwtResponseData;
    }

    /**
     * 校验客户端发送请求时携带的 Token令牌
     * @param request 请求
     * @return jwtResponseData(状态码，业务数据，描述信息，Token令牌)
     */
    @ResponseBody
    @PostMapping(value = "/validateJWT")
    public Object validateJWT(HttpServletRequest request) {
        System.out.println("JWTController.validateJWT");
//        获取客户端携带的 Token令牌
        String token = request.getHeader("Authorization");
//        校验 Token令牌
        JWTResult jwtResult = JWTUtils.validateJWT(token);
        JWTResponseData jwtResponseData = new JWTResponseData();
//        根据校验结果返回信息
        if (jwtResult.isSuccess()) {
            jwtResponseData.setCode(200);
            jwtResponseData.setData(jwtResult.getClaims().getSubject());
//            返回一个新的 Token令牌，重置 Token令牌的有效期
            String newToken = JWTUtils.createJWT(jwtResult.getClaims().getId(), jwtResult.getClaims().getIssuer(),
                    jwtResult.getClaims().getSubject(), (1 * 60 * 1000));
            jwtResponseData.setToken(newToken);
            jwtResponseData.setMessage("Token令牌有效");
            return jwtResponseData;
        } else {
            jwtResponseData.setCode(jwtResult.getErrorCode());
            if (1005 == jwtResult.getErrorCode()) {
                jwtResponseData.setMessage("Token令牌 已过期，请重新登陆");
            } else if (1006 == jwtResult.getErrorCode()) {
                jwtResponseData.setMessage("签名失效或其他错误，请重新登陆");
            }
            return jwtResponseData;
        }
    }
}
