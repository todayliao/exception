package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription 顶部导航栏 nav
 **/
@Getter
public enum TopNavEnum {

    QUESTION("question", "问答"),
    USER("user", "用户墙"),
    BLOG("blog", "博客"),
    ABOUT("about", "关于"),
    ;

    private final String code;
    private final String desc;

    TopNavEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
