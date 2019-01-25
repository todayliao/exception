package com.exception.qms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午1:11
 * @discription 资源不存在
 **/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("The resource not found.");
    }

}
