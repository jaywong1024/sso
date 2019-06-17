package com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {

    private static final String MAC_OS = "Mac OS";
    private static final String WINDOWS = "Windows";
    private static final String LINUX = "Linux";
    private static final String ANDROID = "Android";
    private static final String IPHONE = "iPhone";

    /**
     * 判断客户端类型：
     *     如果为 android 或 ios 返回 1
     *     如果为 win 或 linux 或 mac 返回 2
     *     其他返回 -1
     * @param request 请求
     * @return 1/2/-1
     */
    private int judgingClientType(HttpServletRequest request) {
        String UserAgent = request.getHeader("User-Agent");
        System.out.println(UserAgent);
        if (UserAgent.contains(IPHONE) || UserAgent.contains(ANDROID)) return 1;
        if (UserAgent.contains(MAC_OS) || UserAgent.contains(WINDOWS) || UserAgent.contains(LINUX)) return 2;
        return -1;
    }

    @RequestMapping(value = "/")
    public String restful(HttpServletRequest request) {
        System.out.println(judgingClientType(request));
        return "restful";
    }

}
