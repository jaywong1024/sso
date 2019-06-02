package com.sso.controller;

import com.sso.service.DesService;
import com.sso.utils.DesCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DesController {

    @Autowired
    DesService desService;

    @PostMapping(value = "/des")
    public String des(String ciphertext) {

        System.out.println("从服务端成功获取密文");
        System.out.println("密文: " + ciphertext);
        System.out.println("========== 开始解密 ==========");
        String str = DesCrypt.decrypt(null, ciphertext);
        System.out.println("解密结果:" + str);
        String[] strings = str.split(",");
//        注意: split() 方法切割的第二个字符串 .length() 不正常
//        需要使用 .trim().replace(" ", "") 处理
        if (this.desService.isLogin(strings[0], strings[1].trim().replace(" ", ""))) {
            System.out.println("返回 success");
            return DesCrypt.encrypt(null, "success");
        }
        System.out.println("返回 error");
        return DesCrypt.encrypt(null, "error");
    }

}
