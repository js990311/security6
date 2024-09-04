package com.study.security6.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
public class ProviderUser {
    private String id;
    private String username;
    private String password;
    private String email;
    private String provider;
    private List<? extends GrantedAuthority> authorities;

    public ProviderUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.provider = clientRegistration.getRegistrationId();
        this.id = oAuth2User.getAttribute("sub");
        this.username = oAuth2User.getAttribute("preferred_username");
        this.password = UUID.randomUUID().toString();
        this.email = oAuth2User.getAttribute("email");
        this.authorities = oAuth2User.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(
                        auth.getAuthority()
                )).toList();
    }
}
