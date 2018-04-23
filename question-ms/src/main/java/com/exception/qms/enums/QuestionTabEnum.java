package com.exception.qms.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription 问答 tab
 **/
@Getter
public enum QuestionTabEnum {

    NEW("new", "最新"),
    HOT("hot", "最热"),
    ;

    private final String code;
    private final String desc;

    QuestionTabEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static QuestionTabEnum codeOf(String code) {
        for (QuestionTabEnum questionTabEnum : values()) {
            if (Objects.equals(questionTabEnum.getCode(), code)) {
                return questionTabEnum;
            }
        }
        throw new IllegalArgumentException("code not exited");
    }
}
