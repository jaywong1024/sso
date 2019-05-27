package com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/")
    public String loginPage() {
        System.out.println("pageController.loginPage");
        return "login";
    }

}
