package com.study.security6.domain.jwt.filter;

import com.study.security6.domain.jwt.service.JwtTokenService;
import com.study.security6.security.authentication.MyAuthenticationProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
        this.securityContextLogoutHandler = new SecurityContextLogoutHandler();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization");

        if(auth == null || !auth.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }

        /* 인증 처리 */
        String token = auth.split(" ")[1];
        Claims claims = jwtTokenService.validateToken(token);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                jwtTokenService.getAuthorities(claims)
        );

        // Context에 저장하기
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.getContextHolderStrategy().setContext(context);

        chain.doFilter(request,response);

        // JWT 토큰이므로 clearContext처리
        securityContextLogoutHandler.logout(request, response, authentication);

        SecurityContextHolder.clearContext();
    }
}
