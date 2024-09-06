package com.study.security6.domain.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenService {
    private final Key KEY;
    private final long EXPIRE = 86400000;

    private final JwtParser parser;

    public JwtTokenService(@Value("${jwt.secret}") String secretKey){
        byte[] key = Decoders.BASE64.decode(secretKey);
        this.KEY = Keys.hmacShaKeyFor(key);
        parser = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build();
    }

    public String generateToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = new Date().getTime();
        Date tokenExpire = new Date(now + EXPIRE);

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(tokenExpire)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public Claims validateToken(String token){
        try {
            Claims claims = parser
                    .parseClaimsJws(token)
                    .getBody();
//            사실 claims.parseClaimsJws가 ExpiredJwtException을 throw하기 때문에 아래의 코드는 필요하지 않으ㅁ 
//            if(claims.getExpiration().before(new Date())){
//                // getExpiration < Date (before == '<')
//                throw new TokenExpiredException();
//            }
            return claims;
        }catch (RuntimeException e){
            throw e;
        }
    }

    public List<? extends GrantedAuthority> getAuthorities(Claims claims){
        return Arrays.stream(claims.get("authorities", String.class).split(","))
                .map(SimpleGrantedAuthority::new).toList();
    }
}
