package com.sso.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DesService {

    public static final Map<String, String> USERS = new HashMap<>(16);

    //    静态代码块，它在类加载器第一次加载类时调用
    static {
        USERS.put("admin", "12345");
        System.out.println(USERS);
    }

    public static boolean isLogin(String username, String password) {
        if (null == username || 0 == username.trim().length()) {
            return false;
        }
        String psw = USERS.get(username);
        if (null == psw || !psw.equals(password)) {
            return false;
        }
        return true;
    }

}
