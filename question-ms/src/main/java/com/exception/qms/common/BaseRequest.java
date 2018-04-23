package com.exception.qms.common;

import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午4:25
 * @discription
 **/
@Data
public class BaseRequest<T> implements Serializable {

    @Valid
    protected T data;

}
