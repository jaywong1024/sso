package com.sso.controller;

import com.sso.commons.utils.CookieUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class DoMainController {

    @GetMapping(value = "/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("========== ========== ========== ==========");
        String cookieName = "custom_global_session_id";
        String encodeString = "UTF-8";
//        获取 cookie 的值
        String cookieValue = CookieUtils.getCookieValue(request, cookieName, encodeString);
//        trim()函数:去掉字符串两端的多余的空格
        if (null == cookieValue || "".equals(cookieValue.trim())) {
            System.out.println("无 cookie，生成新的 cookie 数据");
            cookieValue = UUID.randomUUID().toString();
        }
//        设置 cookie 的值
        CookieUtils.setCookie(request, response, cookieName, cookieValue, 0, encodeString);
        System.out.println("========== ========== ========== ==========");
        return "test";
    }

}
