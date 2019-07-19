package com.example.config;

import com.example.TUserService;
import com.example.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jinrun.xie
 * @date 2019/7/18
 **/
@Component("userDetailsService")
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private TUserService userService;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        TUser user = userService.getUserByName(s);
        if (user == null) {
            throw new UsernameNotFoundException("login:" + s + ",not found");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
        grantedAuthorities.add(authority);
        UserDetail userDetail = new UserDetail();
        userDetail.setAuthorities(grantedAuthorities);
        userDetail.setId(user.getId());
        userDetail.setLogin(user.getLogin());
        userDetail.setPassword(user.getPassword());
        return userDetail;
    }
}
