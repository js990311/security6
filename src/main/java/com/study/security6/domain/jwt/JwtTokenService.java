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
    private final long ACCESS_EXPIRATION = 24*60*60; // 하루
    private final long REFRESH_EXPIRATION = 14 * 24*60*60; // 2주

    private final JwtParser parser;

    public JwtTokenService(@Value("${jwt.secret}") String secretKey){
        byte[] key = Decoders.BASE64.decode(secretKey);
        this.KEY = Keys.hmacShaKeyFor(key);
        parser = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build();
    }

    public JwtTokenDto generateToken(Authentication authentication){
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = new Date().getTime();
        Date accessExpiriation = new Date(now + ACCESS_EXPIRATION);
        Date refreshExpirition = new Date(now + REFRESH_EXPIRATION);

        String access = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(now))
                .setExpiration(accessExpiriation)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();

        String refresh = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(now))
                .setExpiration(refreshExpirition)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .claim("isRefresh",true)
                .compact();

        return new JwtTokenDto(access, refresh);
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
