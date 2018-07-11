package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 博客（文章）
 **/
@Controller
public class ArticleController extends BaseController {

    @GetMapping("/article")
    @OperatorLog(description = "博客展示页")
    public String showArticlePage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                  @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                  @RequestParam(value = "tab", defaultValue = "new") String tab,
                                  Model model) {
        model.addAttribute(ResponseModelKeyEnum.TAB.getCode(), tab);
        return "article/article-list";
    }


}
