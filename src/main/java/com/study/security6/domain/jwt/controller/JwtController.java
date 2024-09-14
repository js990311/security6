package com.study.security6.domain.jwt.controller;

import com.study.security6.domain.jwt.dto.JwtTokenDto;
import com.study.security6.domain.jwt.service.JwtTokenService;
import com.study.security6.security.authentication.MyAuthenticationProvider;
import io.jsonwebtoken.Claims;
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
    public JwtTokenDto getToken(@RequestHeader("Authorization") String authorizationHeader){
        String[] auth = authorizationHeader.split(" ");
        String tokenType = auth[0];
        String token = auth[1];

        if(tokenType.equals("Basic")){
            String basicAuthentication = new String(Base64.getDecoder().decode(token));
            String[] split = basicAuthentication.split(":");
            Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(split[0], split[1]);
            Authentication authenticate = myAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
            return jwtTokenService.generateToken(authenticate);
        }else if(tokenType.equals("Bearer")){
            Claims claims = jwtTokenService.validateToken(token);
            Boolean isRefresh = claims.get("isRefresh", Boolean.class);
            if(isRefresh == null || isRefresh == false){
                throw new RuntimeException();
            }
            Authentication usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(claims.getSubject(), null);
            return jwtTokenService.generateToken(usernamePasswordAuthenticationToken);

        }
        throw new RuntimeException();
    }

    @GetMapping("/need-auth")
    public Authentication getNeedAuthBody(Authentication authentication){
        return authentication;
    }

}
