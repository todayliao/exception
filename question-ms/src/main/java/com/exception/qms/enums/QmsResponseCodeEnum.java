package com.exception.qms.enums;

import lombok.Getter;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午12:56
 * @discription qms 响应 code 枚举
 **/
@Getter
public enum QmsResponseCodeEnum {

    SUCCESS("QMS000000", "成功"),
    SYSTEM_ERROR("QMS100000", "出错啦,后台小哥在努力解决bug中..."),
    PARAM_ERROR("QMS100001", "参数错误"),
    QUESTION_NOT_EXIST("QMS100002", "问题不存在"),
    USER_NOT_EXIST("QMS100003", "用户不存在"),
    QUESTION_TAGS_OVER("QMS100004", "问题标签超数"),
    UPLOAD_FILE_NULL("QMS100005", "上传文件不能为空"),
    UPLOAD_FILE_FAIL("QMS100006", "上传文件失败"),
    SEARCH_KEY_EMPTY("QMS100007", "查询的关键字不能为空"),
    ES_STATUS_NOT_OK("QMS100008", "查询 ES 异常"),
    ;

    private final String errorCode;
    private final String errorMessage;

    QmsResponseCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
