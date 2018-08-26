package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.HealthArticleBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 养生文章
 **/
@RestController
public class HealthArticleController extends BaseController {

    @Autowired
    private HealthArticleBusiness healthArticleBusiness;

    @GetMapping("/health/article/list")
    @OperatorLog(description = "首页列表展示")
    public PageQueryResponse queryHealthArticleList(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                    @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        return healthArticleBusiness.queryHealthArticleList(pageIndex, pageSize);
    }

    @GetMapping("/health/article/{articleId}")
    @OperatorLog(description = "获取养生文章详情")
    public BaseResponse getHealthArticleContent(@PathVariable("articleId") Long articleId) {
        return healthArticleBusiness.queryHealthArticleContent(articleId);
    }

}
