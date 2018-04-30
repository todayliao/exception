package com.exception.qms.security;

import net.sf.json.JSONObject;
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

    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    /**
     * 针对 /api 开头的 url 做特殊处理，不走 spring security 默认跳转的登录页面，而是返回 json
     *
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
            response.setContentType("application/json;charset=UTF-8");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false);
            jsonObject.put("message", "您还没登录呢");
            PrintWriter pw = response.getWriter();
            pw.print(jsonObject);
            pw.close();
        } else {
            super.commence(request, response, authException);
        }

    }
}
