package com.exception.qms.common;

import com.alibaba.fastjson.JSON;
import com.exception.qms.enums.QmsResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import site.exception.common.BaseResponse;
import site.exception.exception.BaseException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午4:25
 * @discription controller 全局异常处理器
 **/
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {


    @ExceptionHandler({BaseException.class,
            BindException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class})
    @ResponseBody
    BaseResponse<Object> handleControllerException(HttpServletRequest request, Throwable ex) {
        BaseResponse<Object> baseResponse = new BaseResponse<>();
        String errorCode = null;
        String message = null;
        if (ex instanceof BaseException) {
            BaseException bex = (BaseException) ex;

            //错误码
            errorCode = bex.getErrorCode();
            //错误信息
            message = bex.getMessage();

            log.error("{} request error，errorCode, , {},errorCode {} ,errorMessage {}", request.getRequestURI(), JSON.toJSONString(baseResponse), errorCode, message);
        } else if (ex instanceof BindException) {
            errorCode = QmsResponseCodeEnum.PARAM_ERROR.getErrorCode();
            BindingResult result = ((BindException) ex).getBindingResult();
            StringBuilder stringBuilder = new StringBuilder();
            Optional.ofNullable(result.getFieldErrors()).orElse(new ArrayList<>())
                    .stream().forEach(f -> {
                stringBuilder.append(f.getField())
                        .append(" ")
                        .append(f.getDefaultMessage())
                        .append(", 当前值: '")
                        .append(f.getRejectedValue())
                        .append("'; ");
            });

            message = stringBuilder.toString();

            log.error("{} request error , {}", request.getRequestURI(), message);
        } else if (ex instanceof MethodArgumentNotValidException) {
            //BindException and MethodArgumentNotValidException implements BindingResult
            errorCode = QmsResponseCodeEnum.PARAM_ERROR.getErrorCode();
            BindingResult result = ((MethodArgumentNotValidException) ex).getBindingResult();
            StringBuilder stringBuilder = new StringBuilder();
            Optional.ofNullable(result.getFieldErrors()).orElse(new ArrayList<>())
                    .stream().forEach(f -> {
                stringBuilder.append(f.getField())
                        .append(" ")
                        .append(f.getDefaultMessage())
                        .append(", 当前值: '")
                        .append(f.getRejectedValue())
                        .append("'; ");
            });

            message = stringBuilder.toString();
            log.error("{} request error , {}", request.getRequestURI(), message);
        } else if (ex instanceof ConstraintViolationException) {
            errorCode = QmsResponseCodeEnum.PARAM_ERROR.getErrorCode();
            ConstraintViolationException cex = (ConstraintViolationException) ex;

            StringBuilder stringBuilder = new StringBuilder();
            Optional.ofNullable(cex.getConstraintViolations()).orElse(new HashSet<>()).stream()
                    .forEach(v -> {
                        String invald = v.getInvalidValue() != null ? v.getInvalidValue().toString() : "null";
                        stringBuilder
                                .append(v.getPropertyPath())
                                .append(" ")
                                .append(v.getMessage())
                                .append(", 当前值: '")
                                .append(invald.length() < 50 ? invald : invald.substring(0, 47) + "...")
                                .append("'; ");
                    });
            message = stringBuilder.toString();

            log.error("{} request error , {}", request.getRequestURI(), message);
        } else {
            errorCode = QmsResponseCodeEnum.SYSTEM_ERROR.getErrorCode();
            message = QmsResponseCodeEnum.SYSTEM_ERROR.getErrorMessage();
            log.error(request.getRequestURI() + " request error , ", ex);
        }

        baseResponse.setErrorCode(errorCode);
        baseResponse.setErrorMessage(message);
        baseResponse.setSuccess(false);
        return baseResponse;
    }
}
