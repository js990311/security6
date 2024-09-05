package com.study.security6.domain.oauth2.service;

import com.study.security6.domain.role.user.dto.UserRoleDto;
import com.study.security6.domain.role.user.service.UserRoleService;
import com.study.security6.domain.user.dto.ProviderUser;
import com.study.security6.domain.user.entity.User;
import com.study.security6.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CustomOidcService extends OidcUserService {
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        ProviderUser user = new ProviderUser(oAuth2User, clientRegistration);
        if(!userService.existsByUsername(user.getEmail())){
            userService.createUser(user);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        Collection<? extends GrantedAuthority> userAuthorities = userDetails.getAuthorities();
        List<? extends GrantedAuthority> oidcAuthorities = user.getAuthorities();
        List<GrantedAuthority> combinedAuthorities = new ArrayList<>();
        combinedAuthorities.addAll(userAuthorities);
        combinedAuthorities.addAll(oidcAuthorities);

        return new DefaultOidcUser(
                combinedAuthorities,
                oAuth2User.getIdToken()
        );
    }

}
