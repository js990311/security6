package com.study.security6.config;

import com.study.security6.domain.role.service.RoleService;
import com.study.security6.domain.role.user.service.UserRoleService;
import com.study.security6.security.authentication.MyAuthenticationProvider;
import com.study.security6.security.authentication.UserDetailsServiceImpl;
import com.study.security6.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 스프링 시큐리티7부터는 람다만 지원
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(
                myAuthenticationProvider()
        );

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/need-auth").authenticated()
                        .requestMatchers("/user/regist").permitAll()
                        .anyRequest().authenticated()
        );

        http.formLogin(
                config -> config
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/") // alwaysuse:false ->인증 이전 페이지로 리다이렉트함
                        .failureUrl("/failed")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .permitAll()
        );
        http.logout(
                config -> config
                        .logoutUrl("/logout-proc")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "SOMETHING_WE_NEED_DELETE_COOKIE") // 로그아웃시 삭제해야할 쿠키
//                        .addLogoutHandler(((request, response, authentication) -> {/*새로운 로그아웃 핸들러 추가*/}))
                        .permitAll()
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MyAuthenticationProvider myAuthenticationProvider(){
        return new MyAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    @Bean public UserDetailsServiceImpl userDetailsService(){
        return new UserDetailsServiceImpl(userRepository, userRoleService);
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy(){
        String roleHierarchyString = roleService.getRoleHierarchy();
        RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.fromHierarchy(roleHierarchyString);
        return roleHierarchy;
    }
}
