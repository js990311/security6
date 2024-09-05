package com.study.security6.config;

import com.study.security6.domain.oauth2.service.CustomOidcService;
import com.study.security6.domain.role.service.RoleService;
import com.study.security6.domain.role.user.service.UserRoleService;
import com.study.security6.domain.user.service.UserService;
import com.study.security6.security.authentication.MyAuthenticationProvider;
import com.study.security6.security.authentication.UserDetailsServiceImpl;
import com.study.security6.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
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
    private final ClientRegistrationRepository clientRegistrationRepository;

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
                        .requestMatchers("/oauth2/login").permitAll()
                        .anyRequest().permitAll()
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

        http.oauth2Login(
                config->config
                        .userInfoEndpoint(
                                userInfoEndpointConfig->userInfoEndpointConfig
                                        .oidcUserService(customOidcService())
                        )
                        .defaultSuccessUrl("/")
        );
        http.logout(config-> config
                .logoutSuccessHandler(oidcClientInitiatedLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
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

    @Bean
    public OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler(){
        OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        handler.setPostLogoutRedirectUri("http://localhost:8080/oauth2/login");
        return handler;
    }

    @Bean
    public UserService userService(){
        return new UserService(userRepository, userRoleService, passwordEncoder());
    }

    @Bean
    public CustomOidcService customOidcService(){
        return new CustomOidcService(userService(), userDetailsService());
    }
}
