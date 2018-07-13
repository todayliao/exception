package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription
 **/
@Getter
public enum BaiduLinkPushTypeEnum {

    SUCCESS(0),
    FAIL(1),
    ;

    private final int code;

    BaiduLinkPushTypeEnum(int code) {
        this.code = code;
    }
}
