package com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class DoMainController {

    @GetMapping(value = "/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {

        String cookieName = "custom_global_session_id";
        String encodeString = "UTF-8";



        return "test";
    }

}
