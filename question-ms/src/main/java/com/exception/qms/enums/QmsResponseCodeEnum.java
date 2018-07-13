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
    USER_IS_NULL("QMS100009", "用户信息不能为空"),
    ALREADY_VOTE_UP("QMS100010", "您已经点赞过了哦"),
    ALREADY_VOTE_DOWN("QMS100011", "您已经踩过了哦"),
    UPDATE_CONTENT_NOT_CHANGE("QMS100012", "修改内容未发生实质改变"),
    NOT_CONTAIN_QUESTION_MARK("QMS100013", "你还没有给问题添加问号"),
    TAG_NOT_EXIST("QMS100014", "该标签不存在"),
    ARTICLE_NOT_EXIST("QMS100014", "该文章不存在"),
    ;

    private final String errorCode;
    private final String errorMessage;

    QmsResponseCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
