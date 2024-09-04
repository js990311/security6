package com.study.security6.domain.oauth2.service;

import com.study.security6.domain.user.dto.ProviderUser;
import com.study.security6.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@RequiredArgsConstructor
public class CustomOidcService extends OidcUserService {
    private final UserService userService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        ProviderUser user = new ProviderUser(oAuth2User, clientRegistration);
        if(userService.existsByUsername(user.getEmail())){
//            userService.createUser();
        }
        return oAuth2User;
    }

}
