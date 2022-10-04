package com.inswave.appplatform.core.security;

import com.inswave.appplatform.core.security.jwt.JWTConfigurer;
import com.inswave.appplatform.core.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private final TokenProvider tokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
        //.cors()
        .cors().configurationSource(corsConfigurationSource())
        .and()
        .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.

        .and()
        .authorizeRequests()

        //        .antMatchers("/api/wem/**").permitAll()
        .antMatchers("/api/wem/manager/**").permitAll()
        .antMatchers("/api/wem/log/**").permitAll()
        .antMatchers("/api/wem/deployer/**").permitAll()
        .antMatchers("/monitoring/**").permitAll()

        .antMatchers("/api/wem/terminal/personal-data/**",
                     "/api/wem/terminal/help-reply/**",
                     "/api/wem/terminal/user/**",
                     "/api/wem/ra/TerminalNotice").permitAll()  // 단말관련 외부노출 API

        .antMatchers("/api/wem/authentication/**").permitAll()           // 로그인,아웃 API는 항상노출

        .antMatchers("/api/dbtool/**").permitAll()           // DB tool..

        //               	.antMatchers("/api/wem/manager","/api/wem/log").hasAuthority(RoleType.ADMIN.toString())
        //                .antMatchers("/api/wem/manager","/api/wem/log").hasAuthority(RoleType.MANAGEMENT.toString())
        //                .antMatchers("/api/wem/manager","/api/wem/log").hasAuthority(RoleType.USER.toString())

        //                	.antMatchers("/api/InitEnvironment","/api/Login").permitAll()
        //                	.antMatchers("/api/mybatis.mapper/**").hasAuthority("ADMIN")
        //                	.antMatchers("/api/service/**").hasAuthority("ADMIN")

        //					WebSocketConfig에서 잘 안먹혀서 FilterChannelInterceptor 에서 구현함 ?????????????????????????
        //                	.antMatchers("/api/**").permitAll()
        //                	.antMatchers("/api/agent/connectUser").permitAll()
        //                	.antMatchers("/api/agent/AgentInstall").permitAll()
        //                	.antMatchers("/api/agent/AgentLogin").permitAll()
        //                	.antMatchers("/api/agent/service/**").hasAuthority("AGENT")

        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .httpBasic()
        .and()
        .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Override // ignore security config
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/test.html");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        //configuration.setAllowCredentials(true); // configuration.addAllowedOrigin("*"); 와 동시에 못씀
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return charSequence.equals(s);
            }
        };
    }

}