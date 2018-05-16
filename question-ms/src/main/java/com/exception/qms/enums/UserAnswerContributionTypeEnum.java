package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription
 **/
@Getter
public enum UserAnswerContributionTypeEnum {

    CREATE(1, "发布问题解决方案"),
    EDIT(2, "改进问题解决方案"),
    ;

    private final int code;
    private final String desc;

    UserAnswerContributionTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
