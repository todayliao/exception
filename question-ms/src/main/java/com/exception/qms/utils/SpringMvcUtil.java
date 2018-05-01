package com.exception.qms.utils;

import com.exception.qms.domain.entity.User;
import org.springframework.security.core.context.SecurityContext;

import javax.servlet.http.HttpSession;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription
 **/
public class SpringMvcUtil {


    /**
     * 获取 session 中保存的当前登陆者的信息
     * @return
     */
    public static User getCurrentLoginUser(HttpSession session) {
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = null;
        if (securityContext != null
                && securityContext.getAuthentication() != null
                && securityContext.getAuthentication().getPrincipal() != null) {
            user = (User) securityContext.getAuthentication().getPrincipal();
        }
        return user;
    }


}
