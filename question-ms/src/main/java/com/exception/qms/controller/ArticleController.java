package com.exception.qms.controller;

import com.exception.qms.business.ArticleBusiness;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.utils.SpringMVCUtil;
import com.exception.qms.model.form.article.ArticleForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.exception.common.BaseResponse;

import javax.servlet.http.HttpSession;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 博客（文章）
 **/
@Controller
public class ArticleController {

    @Autowired
    private ArticleBusiness articleBusiness;

    @GetMapping("/article/write")
    public String showArticleWritePage() {
        return "article/article-write";
    }

    @PostMapping("/article")
    @ResponseBody
    public BaseResponse commitArticle(@Validated ArticleForm articleForm, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return articleBusiness.commitArticle(articleForm, user == null ? null : user.getId());
    }

    @GetMapping("/article/{articleId}")
    public String showArticleDetail(@PathVariable("articleId") Long articleId, Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), articleBusiness.queryArticleDetail(articleId));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.ARTICLE.getCode());
        return "article/article-detail";
    }
}
