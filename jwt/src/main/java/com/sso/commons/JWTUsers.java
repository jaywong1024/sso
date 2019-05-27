package com.sso.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于模拟用户数据，开发中应访问数据库验证用户
 */
public class JWTUsers {

    public static final Map<String, String> USERS = new HashMap<>(16);

//    静态代码块，它在类加载器第一次加载类时调用
    static {
        for (int i = 0; i < 10; i++) {
            USERS.put("username" + i, "password" + i);
        }
        System.out.println(USERS);
    }

    public static boolean isLogin(String username, String password) {
        if (null == username || 0 == username.trim().length()) return false;
        String psw = USERS.get("username");
        if (null == psw || !psw.equals(password)) {
            return false;
        }
        return true;
    }

}
