package com.study.security6.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class IndexController {
    @GetMapping("")
    public String getIndex(){
        return "index";
    }

    @GetMapping("/need-auth")
    public String getNeedAuth(Authentication authentication){
        if(authentication != null){
            log.info("isAuthenticated?", authentication.isAuthenticated());
        }
        return "need-auth";
    }
}
