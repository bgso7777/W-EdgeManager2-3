package com.inswave.appplatform.core.security;

import com.inswave.appplatform.dao.AdminUserDao;
import com.inswave.appplatform.domain.AdminUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private AdminUserDao adminUserDao;

    public DomainUserDetailsService(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("인증 - {}", username);
        return adminUserDao.findByLoginId(username)
                                 .map(user -> createSpringSecurityUser(username, user))
                                 .orElseThrow(() -> new UsernameNotFoundException("User [" + username + "] was not found in the database"));
    }

    private DomainUserDetails createSpringSecurityUser(String login, AdminUser adminUser) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>() {
            {
                add(AuthoritiesConstants.ROLE_ADMIN);
            }
        };
        return new DomainUserDetails(adminUser.getName(), adminUser.getLoginId(), adminUser.getPassword(), authorities);
    }

//    public DomainUserDetailsService() {
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.debug("인증 - {}", username);
//        Config.JwtUserDetailsUser user = Config.getInstance().getUsers().get(username);
//        if(user==null)
//            new UsernameNotFoundException("User [" + username + "] was not found in the database");
//        return createSpringSecurityUser(username, user);
//    }
//
//    private DomainUserDetails createSpringSecurityUser(String login, Config.JwtUserDetailsUser user) {
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>() {
//            {
//                add(AuthoritiesConstants.ROLE_ADMIN);
//            }
//        };
//        return new DomainUserDetails(user.getName(), user.getLoginId(), user.getPassword(), authorities);
//    }

}
