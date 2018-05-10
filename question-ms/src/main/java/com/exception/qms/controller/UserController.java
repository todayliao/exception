package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class UserController extends BaseController {

    @Autowired
    private UserBusiness userBusiness;

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/user/login")
    @OperatorLog(description = "登录页")
    public String showUserLoginPage(Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        return "user/user-login";
    }

    @GetMapping("/user")
    @OperatorLog(description = "展示用户墙")
    public String showUser(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                           @RequestParam(value = "pageSize", defaultValue = "36") Integer pageSize,
                           @RequestParam(value = "tab", defaultValue = "new") String tab,
                           Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), userBusiness.queryUserPageList(pageIndex, pageSize, tab));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        return "user/user-wall";
    }

}
