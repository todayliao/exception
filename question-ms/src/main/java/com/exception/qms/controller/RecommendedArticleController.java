package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.RecommendedArticleBusiness;
import com.exception.qms.common.ControllerExceptionHandler;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 优文
 **/
@Controller
public class RecommendedArticleController {

    @Autowired
    private RecommendedArticleBusiness recommendedArticleBusiness;

    /**
     * 优文列表分页
     * @return
     */
    @GetMapping("/recommended/article")
    @OperatorLog(description = "优文列表分页展示")
    public String queryRecommendedArticleList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), recommendedArticleBusiness.queryRecommendedArticleList(pageIndex, pageSize));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.RECOMMENDED_ARTICLE.getCode());
        return "recommendedArticle/article-list";
    }

    @GetMapping("/recommended/article/{articleId}")
//    @OperatorLog(description = "优文详情展示页")
    public String showArticleDetail(@PathVariable("articleId") Long articleId, Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), recommendedArticleBusiness.queryArticleDetail(articleId));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.RECOMMENDED_ARTICLE.getCode());
        return "recommendedArticle/article-detail";
    }
}
