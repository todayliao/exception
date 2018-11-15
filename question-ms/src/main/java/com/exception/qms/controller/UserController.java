package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.ControllerExceptionHandler;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.exception.common.BaseResponse;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class UserController {

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
    public String showUserWall(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "pageSize", defaultValue = "36") Integer pageSize,
                               @RequestParam(value = "tab", defaultValue = "new") String tab,
                               Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), userBusiness.queryUserPageList(pageIndex, pageSize, tab));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        return "user/user-wall";
    }

    @GetMapping("/user/{userId}")
    @OperatorLog(description = "展示用户")
    public String showUser(@PathVariable("userId") Long userId,
                           @RequestParam(value = "tab", defaultValue = "question") String tab,
                           Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), userBusiness.queryUserDetail(userId, tab));
        model.addAttribute("userId", userId);
        return "user/user-detail";
    }

    @GetMapping("/user/{userId}/contribution/data")
    @OperatorLog(description = "用户贡献数据")
    @ResponseBody
    public BaseResponse queryContributionData(@PathVariable("userId") Long userId) {
        return userBusiness.queryContributionData(userId);
    }

}
