package com.exception.qms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class ErrorPageController {

    @GetMapping("/404")
    public String show404Page() {
        return "error/404";
    }

    @GetMapping("/500")
    public String show500Page() {
        return "error/500";
    }

}
