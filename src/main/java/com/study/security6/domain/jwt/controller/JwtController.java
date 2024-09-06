package com.study.security6.domain.jwt.controller;

import com.study.security6.domain.jwt.JwtTokenService;
import com.study.security6.security.authentication.MyAuthenticationProvider;
import com.study.security6.security.authentication.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RequiredArgsConstructor
@RequestMapping("/jwt")
@RestController
public class JwtController {
    private final JwtTokenService jwtTokenService;
    private final MyAuthenticationProvider myAuthenticationProvider;

    @GetMapping("/token")
    public String loginForGetToken(@RequestHeader("Authorization") String basicAuthentication){
        String decodeAuthentication = new String(Base64.getDecoder().decode(basicAuthentication));
        String[] split = decodeAuthentication.split(":");
        Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(split[0], split[1]);
        Authentication authenticate = myAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        return jwtTokenService.generateToken(authenticate);
    }
}
