package com.exception.qms.security;

import com.exception.qms.domain.enhancement.AuthUser;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/9
 * @time 下午4:54
 * @discription
 **/
@Slf4j
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    /**
     * 加盐
     */
    private static final String PASSWORD_SALT = "qwefdau131954930&(^%$%&**!(@#";

    private final Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String inputPassword = (String) authentication.getCredentials();

        User user = userService.queryByUserName(userName);
        if (user == null) {
            log.warn("the userName:{} is not exsit", userName);
            throw new AuthenticationCredentialsNotFoundException("auth error");
        }

        AuthUser authUser = mapper.map(user, AuthUser.class);

        // roles
        List<GrantedAuthority> grantedAuthorityList = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        authUser.setGrantedAuthorityList(grantedAuthorityList);

        if (md5PasswordEncoder.isPasswordValid(authUser.getPassword(), inputPassword, PASSWORD_SALT)) {
            return new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
        }

        throw new AuthenticationCredentialsNotFoundException("auth error");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    public static void main(String[] args) {
        String password = "jianghui" ;
        final Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        System.out.println(md5PasswordEncoder.encodePassword(password, PASSWORD_SALT));
    }
}
