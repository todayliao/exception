package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
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
    @OperatorLog(description = "404页面")
    public String show404Page() {
        return "error/404";
    }

    @GetMapping("/500")
    @OperatorLog(description = "500页面")
    public String show500Page() {
        return "error/500";
    }

}
