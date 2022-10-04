package com.inswave.appplatform.core.security.jwt;

import com.inswave.appplatform.core.security.DomainUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String REAL_NAME_KEY   = "real_name";

    private Long   tokenValidityInMilliseconds;
    private String secret;
    private String secretBase64;

    public TokenProvider(@Value("${wedgemanager.jwt.tokenValidityInMilliseconds:#{86400000}}") Long tokenValidityInMilliseconds,
                         @Value("${wedgemanager.jwt.secret:#{'wedgemanagerjwtsecretparsetargettobase64'}}") String secret) {
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
        this.secret = secret;
    }

    @PostConstruct
    public void init() {
        secretBase64 = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        DomainUserDetails user = (DomainUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                   .setSubject(authentication.getName())
                   .claim(AUTHORITIES_KEY, authorities)
                   .claim(REAL_NAME_KEY, user.getRealName())
                   .signWith(SignatureAlgorithm.HS512, secretBase64)
                   .setExpiration(validity)
                   .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secretBase64)
                            .parseClaimsJws(token)
                            .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                                                                   .map(SimpleGrantedAuthority::new)
                                                                   .collect(Collectors.toList());
        String realName = (String) claims.get(REAL_NAME_KEY);

        DomainUserDetails principal = new DomainUserDetails(realName, claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String authToken) {
        try {
            if(authToken==null||authToken.equals(""))
                return false;
            Jwts.parser().setSigningKey(secretBase64).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e1) {
            log.error("ExpiredJwtException : {}"+" authToken-->>"+authToken, e1);
        } catch (JwtException e2) {
            log.error("JwtException : {}"+" authToken-->>"+authToken, e2);
        } catch (IllegalArgumentException e3) {
            log.error("IllegalArgumentException : {}"+" authToken-->>"+authToken, e3);
        }
        return false;
    }

    public Long getTokenValidityInMilliseconds() {
        return tokenValidityInMilliseconds;
    }
}
