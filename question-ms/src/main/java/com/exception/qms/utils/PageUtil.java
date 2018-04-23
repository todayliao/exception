package com.exception.qms.utils;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:37
 * @discription 分页工具类
 **/
public class PageUtil {

    public static int calculateLimitSelectSqlStart(int pageIndex, int pageSize) {
        return (pageIndex - 1) * pageSize;
    }
}
