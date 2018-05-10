package com.exception.qms.service.impl;

import com.exception.qms.domain.enhancement.AuthUser;
import com.exception.qms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author jiangbing(江冰)
 * @date 2018/5/4
 * @time 下午3:40
 * @discription 处理用户信息获取逻辑
 **/
@Slf4j
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // query the user info by the username
        com.exception.qms.domain.entity.User user = userService.queryByUserName(username);
        if (user == null) {
            log.warn("the username: {} is not exist", username);
            throw new UsernameNotFoundException("用户名不存在");
        }

        AuthUser authUser = mapper.map(user, AuthUser.class);

        // roles
        List<GrantedAuthority> grantedAuthorityList = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        authUser.setGrantedAuthorityList(grantedAuthorityList);

        return authUser;
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("xulailing"));
        // $2a$10$ia01BqzjQO15b4FoU6hfteJbEyS6TvrgmJ1N2nH1lZB
    }
}
