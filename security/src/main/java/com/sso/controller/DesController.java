package com.sso.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DesController {

    @PostMapping(value = "/des")
    public String des() {



        return null;
    }

}
