package com.exception.qms.security;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.utils.HttpUtil;
import com.exception.qms.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/28
 * @time 下午3:35
 * @discription 自定义登录成功处理器
 **/
@Slf4j
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 开启 useReferer, 默认情况下关闭的
        String returnUrl = request.getHeader("Referer");
        // 如果是从登录页面登录成功(非异步)的，则跳转首页
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean isFromLoginPage = antPathMatcher.match("**/user/login**", returnUrl);
        if (!isFromLoginPage) {
            super.setUseReferer(true);
        }

        // 是否异步登录，异步登录，则返回 json
        boolean isAjax = HttpUtil.isAjaxRequest(request);
        if (isAjax) {
            response.setContentType(HttpUtil.JSON_UTF8_CONTENT_TYPE);
            BaseResponse baseResponse = new BaseResponse();
            PrintWriter pw = response.getWriter();
            pw.print(JsonUtil.toString(baseResponse.success()));
            pw.close();
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }

}
