package com.exception.qms.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/28
 * @time 下午8:00
 * @discription 自定义登录策略
 **/
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private static final String API_PREFIX = "/api";
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private static final String NEED_LOGIN_RESPONSE_JSON = "{\n" +
            "  \"success\": false,\n" +
            "  \"message\": \"您需要登录账号\"\n" +
            "}";


    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    /**
     * 异常处理
     * Performs the redirect (or forward) to the login form URL.
     *
     * @param request
     * @param response
     * @param authException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
        if (uri.startsWith(API_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(CONTENT_TYPE);

            PrintWriter pw = response.getWriter();
            pw.write(NEED_LOGIN_RESPONSE_JSON);
            pw.close();
        } else {
            super.commence(request, response, authException);
        }

    }
}
