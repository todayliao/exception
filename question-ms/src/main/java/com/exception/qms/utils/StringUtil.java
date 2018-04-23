package com.exception.qms.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.util.Random;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription
 **/
public class StringUtil {

    /**
     * 截取字符串
     * @param originalStr 原始的字符串
     * @param limit 截取的长度
     * @return
     */
    public static String subString(String originalStr, int limit) {
        if (StringUtils.isBlank(originalStr)) {
            return null;
        }

        final int len = originalStr.length();
        if (len - 1 <= limit) {
            return originalStr;
        }

        return originalStr.substring(0, limit);
    }

}
