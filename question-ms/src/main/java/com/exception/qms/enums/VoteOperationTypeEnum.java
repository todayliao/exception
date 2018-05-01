package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription 点赞操作
 **/
@Getter
public enum VoteOperationTypeEnum {

    UP(1, "增加"),
    DOWN(-1, "减少"),
    ;

    private final int code;
    private final String desc;

    VoteOperationTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static VoteOperationTypeEnum codeOf(int code) {
        for (VoteOperationTypeEnum voteOperationEnum : values()) {
            if (voteOperationEnum.getCode() == code) {
                return voteOperationEnum;
            }
        }
        return null;
    }
}
