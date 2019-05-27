package com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping(value = "/login")
    public String login() {
        System.out.println("pageController.login");
        return "login";
    }

}
