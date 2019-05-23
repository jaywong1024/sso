package com.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SpringSessionController {

    @GetMapping(value = "/test")
    public String springSession(HttpServletRequest request, HttpServletResponse response) {
        Object attributeValue = request.getSession().getAttribute("testAttributeName");
        if (null == attributeValue) {
            request.getSession().setAttribute("testAttributeName", "testAttributeValue");
        }
        System.out.println("80: " + attributeValue);
        return "spring-session";
    }

}
