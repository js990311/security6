package com.study.security6.controller;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @GetMapping("/login")
    public String login(){
        return "oauth2/login";
    }

    @ResponseBody
    @GetMapping("/user-info")
    public Authentication userInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User){
        return authentication;
    }
}
