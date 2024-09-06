package com.study.security6.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenServiceTest {
    private final JwtTokenService jwtTokenService;
    private final List<GrantedAuthority> authorities;
    private final String key = "0ed89e38998c31d591261887d37e2148c8dea714330af0febac9b9d22e62517c";

    public JwtTokenServiceTest() {
        jwtTokenService = new JwtTokenService(key);
        authorities = new ArrayList<>();
        for(int i=0;i<10;i++){
            authorities.add(new SimpleGrantedAuthority(String.format("ROLE_%d", i)));
        }
    }

    @Test
    void generateToken() {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("username", "password", authorities);
        String token = jwtTokenService.generateToken(authenticationToken);
        assertNotEquals(null, token);
        assertFalse(token.isBlank());
    }

    @Test
    void validateToken() {
        String username = "username";
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, "password", authorities);
        String token = jwtTokenService.generateToken(authenticationToken);
        Claims claims = jwtTokenService.validateToken(token);

        assertEquals(claims.getSubject(), username);
        assertFalse(claims.getExpiration().before(new Date()));
        assertEquals(authorities.size(), jwtTokenService.getAuthorities(claims).size());
    }

    @Test
    void expireToken(){
        byte[] byteKey = Decoders.BASE64.decode(key);
        Key tmpKey = Keys.hmacShaKeyFor(byteKey);
        long now = new Date().getTime();
        Date beforeExpire = new Date(now - 86400000);

        String token = Jwts.builder()
                .setSubject("username")
                .claim("authorities", authorities)
                .setExpiration(beforeExpire)
                .signWith(tmpKey, SignatureAlgorithm.HS256)
                .compact();
        assertThrows(ExpiredJwtException.class,()->{
            jwtTokenService.validateToken(token);
        });
    }

}