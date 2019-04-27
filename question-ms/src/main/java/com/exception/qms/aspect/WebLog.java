package com.exception.qms.aspect;

import java.lang.annotation.*;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
    /**
     * 日志方法描述
     *
     * @return
     */
    String description() default "";

}

