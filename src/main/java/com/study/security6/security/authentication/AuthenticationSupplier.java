package com.study.security6.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.function.Supplier;

public class AuthenticationSupplier implements Supplier<Authentication> {
    @Override
    public Authentication get() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
