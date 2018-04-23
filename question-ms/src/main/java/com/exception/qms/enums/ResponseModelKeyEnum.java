package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription model key
 **/
@Getter
public enum ResponseModelKeyEnum {

    RESPONSE("response", "返回值"),
    TOP_NAV("topNav", "顶部导航栏标识"),
    TAB("tab", "tab标识"),
    ;

    private final String code;
    private final String desc;

    ResponseModelKeyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
