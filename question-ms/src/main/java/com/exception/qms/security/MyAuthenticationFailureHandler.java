package com.exception.qms.security;

import com.exception.qms.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import site.exception.common.BaseResponse;
import site.exception.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/28
 * @time 下午3:35
 * @discription 自定义登录失败处理器
 **/
@Slf4j
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public MyAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 是否异步登录，异步登录，则返回 json
        boolean isAjax = HttpUtil.isAjaxRequest(request);
        if (isAjax) {
            response.setContentType(HttpUtil.JSON_UTF8_CONTENT_TYPE);
            BaseResponse baseResponse = new BaseResponse();
            PrintWriter pw = response.getWriter();
            pw.print(JsonUtil.toString(baseResponse.fail("登录失败,请检查用户名或密码是否正确！")));
            pw.close();
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
