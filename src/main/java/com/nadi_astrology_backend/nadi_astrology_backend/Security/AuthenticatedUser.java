package com.nadi_astrology_backend.nadi_astrology_backend.Security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class AuthenticatedUser {

    private final Long userId;
    private final String email;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;
}