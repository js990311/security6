package com.study.security6.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

@Getter
public abstract class ProviderUser {
    private String id;
    private String username;
    private String password;
    private String email;
    private String provider;
    private List<? extends GrantedAuthority> authorities;

    public ProviderUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.username = oAuth2User.getAttribute("sub");
        this.provider = clientRegistration.getRegistrationId();
    }
}
