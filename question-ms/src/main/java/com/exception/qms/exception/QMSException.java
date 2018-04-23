package com.exception.qms.exception;

import com.exception.qms.enums.QmsResponseCodeEnum;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午1:11
 * @discription
 **/
public class QMSException extends BaseException {

    public QMSException(String errorCode, String message) {
        super(errorCode, message);
    }

    public QMSException(Throwable cause) {
        super(cause);
    }

    public QMSException(QmsResponseCodeEnum qmsResponseCodeEnum) {
        super(qmsResponseCodeEnum.getErrorCode(), qmsResponseCodeEnum.getErrorMessage());
    }
}
