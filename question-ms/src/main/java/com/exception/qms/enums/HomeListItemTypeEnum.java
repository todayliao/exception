package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription
 **/
@Getter
public enum HomeListItemTypeEnum {

    QUESTION(0, "问题"),
    ARTICLE(1, "文章"),
    ;

    private final Integer code;
    private final String desc;

    HomeListItemTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
