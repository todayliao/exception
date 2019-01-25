package com.exception.qms.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription
 **/
public class HttpUtil {

    public static final String JSON_UTF8_CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 是否是异步请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxHeader = request.getHeader("X-Requested-With");
        return Objects.equals("XMLHttpRequest", ajaxHeader);
    }


}
