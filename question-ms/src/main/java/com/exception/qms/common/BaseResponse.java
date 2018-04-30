package com.exception.qms.common;

import java.io.Serializable;

import com.exception.qms.exception.BaseException;
import lombok.Data;

/**
 * 服务响应 response
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午8:45
 * @discription
 **/
@Data
public class BaseResponse<T> implements Serializable {

    private boolean success = false;
    private String errorCode;
    private String errorMessage;
    private T data;

    public BaseResponse<T> fail() {
        this.setSuccess(false);
        return this;
    }

    public BaseResponse<T> fail(String errorMessage) {
        this.setSuccess(false);
        this.setErrorMessage(errorMessage);
        return this;
    }

    public BaseResponse<T> fail(BaseException baseException) {
        this.setSuccess(false);
        this.setErrorCode(baseException.getErrorCode());
        this.setErrorMessage(baseException.getMessage());
        return this;
    }

    public BaseResponse<T> success(T data) {
        this.setSuccess(true);
        this.setData(data);
        return this;
    }

    public BaseResponse<T> success() {
        this.setSuccess(true);
        return this;
    }
}
