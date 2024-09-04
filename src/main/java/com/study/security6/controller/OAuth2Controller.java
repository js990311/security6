package com.study.security6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping("/login")
    public String login(){
        return "oauth2/login";
    }
}
