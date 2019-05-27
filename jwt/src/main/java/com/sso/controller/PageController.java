package com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = "/loginPage")
    public String loginPage() {
        System.out.println("pageController.loginPage");
        return "login";
    }

}
