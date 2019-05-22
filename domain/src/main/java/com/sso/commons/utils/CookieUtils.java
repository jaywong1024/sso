package com.sso.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtils {

    /**
     * 得到 cookie 域名
     * @param request 请求
     * @return cookie 域名
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName = null;
//        获取完整的请求地址
        String requestURL = request.getRequestURL().toString();
        if (null == requestURL || "".equals(requestURL)) {
            domainName = "";
        } else {
//            转换为小写
            requestURL = requestURL.toLowerCase();
//            在请求地址中去除 http:// 或 https:// 这两种协议的前缀
            String http = "http://", https = "https://";
            if (requestURL.startsWith(http)) {
                requestURL = requestURL.substring(http.length());
            } else if (requestURL.startsWith(https)) {
                requestURL = requestURL.substring(https.length());
            }
//            去除 / 以后的字符串
            final int end = requestURL.indexOf("/");
            if (-1 != end) {
                requestURL = requestURL.substring(0, end);
            }
//            分割到只剩 xxx.xxx:8080 格式
            final String[] domains = requestURL.split("\\.");
            int lenght = domains.length;
            if (lenght > 3) {
//                在 Tomcat 8.0 以上版本前缀有 "." 会报错
//                domainName = "." + domains[lenght - 3] + "." + domains[lenght - 2] + "." + domains[lenght - 1];
                domainName = domains[lenght - 3] + "." + domains[lenght - 2] + "." + domains[lenght - 1];
            } else if (lenght > 1 && lenght <= 3) {
//                在 Tomcat 8.0 以上版本前缀有 "." 会报错
//                domainName = "." + domains[lenght - 2] + "." + domains[lenght - 1];
                domainName = domains[lenght - 2] + "." + domains[lenght - 1];
            } else {
                domainName = requestURL;
            }
//            分割到只剩 xxx.xxx 格式
            if (null != domainName && domainName.indexOf(":") > 0) {
                String[] strings = domainName.split("\\:");
                domainName = strings[0];
            }
        }
        System.out.println("domainName: " + domainName);
        return domainName;
    }

    /**
     * 设置 cookie 的值
     * @param request 请求
     * @param response 响应
     * @param cookieName cookie 名称
     * @param cookieValue cookie 值
     * @param cookieMaxAge cookie 生命周期
     * @param encodeString 编码格式
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxAge, String encodeString) {
        if (cookieValue == null) {
            cookieValue = "";
        } else {
            try {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (cookieMaxAge > 0) {
            cookie.setMaxAge(cookieMaxAge);
        }
        if (null != request) {
            String domainName = getDomainName(request);
            if (!"localhost".equals(domainName)) {
                cookie.setDomain(domainName);
            }
        }
//        cookie 的有效地址: 根路径往后的所有路径都有效，例如 /test
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 得到 cookie 的值
     * @param request 请求
     * @param cookieName cookie 名称
     * @param encodeString 解码格式
     * @return cookie 的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookieName == null) return null;
        String returnValue = null;
        try {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(cookieName)) {
//                    解码获取 cookie 的值
                    returnValue = URLDecoder.decode(cookies[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnValue;
    }


}
