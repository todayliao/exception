package com.exception.qms.config;

import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * @author jiangbing(江冰)
 * @date 2019/1/25
 * @time 13:05
 * @discription 错误页面注册
 **/
public class MyErrorPageRegistrar implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
        registry.addErrorPages(errorPage404, errorPage500);
    }
}
