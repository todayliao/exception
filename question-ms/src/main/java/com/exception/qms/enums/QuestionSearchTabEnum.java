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
public enum QuestionSearchTabEnum {

    RELEVANCE("relevance", "相关度"),
    VOTE("vote", "点赞数"),
    VIEW("view", "浏览数"),
    NEW("new", "最新"),
    ;

    private final String code;
    private final String desc;

    QuestionSearchTabEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static QuestionSearchTabEnum codeOf(String code) {
        for (QuestionSearchTabEnum questionTabEnum : values()) {
            if (Objects.equals(questionTabEnum.getCode(), code)) {
                return questionTabEnum;
            }
        }
        throw new IllegalArgumentException("code not exited");
    }
}
