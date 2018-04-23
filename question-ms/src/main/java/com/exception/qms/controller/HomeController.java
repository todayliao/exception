package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.HomeBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.enums.QuestionTabEnum;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * 首页
     * @return
     */
    @GetMapping("/")
    @OperatorLog(description = "首页 -> forward")
    public String queryQuestionList() {
        return "forward:/home";
    }

    /**
     * 首页问题列表分页
     * @return
     */
    @GetMapping("/home")
    @OperatorLog(description = "首页问题列表分页")
    public String queryQuestionList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                    @RequestParam(value = "tab", defaultValue = "new") String tab,
                                    Model model) {
         model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), homeBusiness.queryQuestionPageList(pageIndex, pageSize, tab));
         model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.QUESTION.getCode());
         model.addAttribute(ResponseModelKeyEnum.TAB.getCode(), tab);
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
