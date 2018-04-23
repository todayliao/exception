package com.exception.qms.enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription
 **/
@Getter
public enum QuestionTypeEnum {

    COMPUTER_TRANSLATE(0, "机器翻译"),
    PEOPLE_TRANSLATE(1, "人工校译"),
    PEOPLE_POST(2, "人工提交"),
    ;

    private final Integer code;
    private final String desc;

    QuestionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
