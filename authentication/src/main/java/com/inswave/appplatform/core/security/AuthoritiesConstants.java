package com.inswave.appplatform.core.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN     = "ROLE_ADMIN";
    public static final String USER      = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final SimpleGrantedAuthority ROLE_ADMIN     = new SimpleGrantedAuthority(ADMIN);
    public static final SimpleGrantedAuthority ROLE_USER      = new SimpleGrantedAuthority(USER);
    public static final SimpleGrantedAuthority ROLE_ANONYMOUS = new SimpleGrantedAuthority(ANONYMOUS);

    private AuthoritiesConstants() {
    }
}
