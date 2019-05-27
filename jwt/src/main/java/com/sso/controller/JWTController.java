package com.sso.controller;

import com.sso.commons.JWTResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JWTController {

    @ResponseBody
    @PostMapping(value = "/login")
    public Object login(String username, String password) {
        JWTResponseData jwtResponseData = null;



        return jwtResponseData;
    }

}
