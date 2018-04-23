package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private QuestionBusiness questionBusiness;

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/login")
    @OperatorLog(description = "登录页")
    public String showUserLoginPage() {
        return "user/user-login";
    }

}
