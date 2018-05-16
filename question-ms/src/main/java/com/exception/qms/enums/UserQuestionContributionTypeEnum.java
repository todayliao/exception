package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription
 **/
@Getter
public enum UserQuestionContributionTypeEnum {

    CREATE(1, "发布问题"),
    EDIT(2, "改进问题"),
    ;

    private final int code;
    private final String desc;

    UserQuestionContributionTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
