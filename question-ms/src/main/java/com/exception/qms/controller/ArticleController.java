package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.web.form.article.ArticleForm;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 博客（文章）
 **/
@Controller
public class ArticleController extends BaseController {

    @GetMapping("/article/write")
    @OperatorLog(description = "博客写作页")
    public String showArticleWritePage() {
        return "article/article-write";
    }

    @PostMapping("/article")
    @OperatorLog(description = "提交博客")
    @ResponseBody
    public BaseResponse commitArticle(ArticleForm articleForm) {
        System.out.println(articleForm);
        return null;
    }

}
