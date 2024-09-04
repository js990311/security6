package com.study.security6.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class Oauth2ClientConfig {
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((request)->request
                .requestMatchers("/oauth2/login").permitAll()
                .requestMatchers("/oauth2/**").authenticated()
                .anyRequest().permitAll()
        );
        http.oauth2Login(Customizer.withDefaults());
        http.logout(config-> config
                .logoutSuccessHandler(oidcClientInitiatedLogoutSuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
        );
        http.oauth2Client(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public OidcClientInitiatedLogoutSuccessHandler oidcClientInitiatedLogoutSuccessHandler(){
        OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        handler.setPostLogoutRedirectUri("http://localhost:8080/oauth2/login");
        return handler;
    }


}
