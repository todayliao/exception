package com.exception.qms.business;

import com.exception.qms.domain.entity.User;
import com.exception.qms.web.form.article.ArticleForm;
import com.exception.qms.web.vo.article.ArticleDetailResponseVO;
import site.exception.common.BaseResponse;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface ArticleBusiness {

    BaseResponse commitArticle(ArticleForm articleForm, Long userId);

    ArticleDetailResponseVO queryArticleDetail(Long articleId);
}
