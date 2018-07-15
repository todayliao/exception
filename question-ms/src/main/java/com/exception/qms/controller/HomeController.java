package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.HomeBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class HomeController extends BaseController {

    @Autowired
    private HomeBusiness homeBusiness;

    /**
     * 首页问题列表分页
     * @return
     */
    @GetMapping({"", "/home"})
    @OperatorLog(description = "首页列表展示")
    public String queryHomeList(@RequestParam(value = "qLimitTime", defaultValue = "") String qLimitTime,
                                @RequestParam(value = "aLimitTime", defaultValue = "") String aLimitTime,
//                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                Model model) {
         model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), homeBusiness.queryHomePageList(qLimitTime, aLimitTime));
         model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.HOME.getCode());
         return "home";
    }

    /**
     * 首页热门标签
     * @return
     */
    @GetMapping("/home/hot/tag/list")
    @OperatorLog(description = "首页热门标签")
    @ApiOperation("首页热门标签")
    @ResponseBody
    public BaseResponse queryHotTags() {
        return homeBusiness.queryHotTags();
    }

    /**
     * 首页热门问题
     * @return
     */
    @GetMapping("/home/hot/question/list")
    @OperatorLog(description = "首页热门问题")
    @ApiOperation("首页热门问题")
    @ResponseBody
    public BaseResponse queryHotQuestions() {
        return homeBusiness.queryHotQuestions();
    }

}
