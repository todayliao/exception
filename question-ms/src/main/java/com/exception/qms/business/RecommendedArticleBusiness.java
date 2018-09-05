package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.web.form.article.ArticleForm;
import com.exception.qms.web.vo.article.ArticleDetailResponseVO;
import com.exception.qms.web.vo.article.QueryRecommendedArticleListItemResponseVO;
import com.exception.qms.web.vo.article.RecommendedArticleDetailResponseVO;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface RecommendedArticleBusiness {

    RecommendedArticleDetailResponseVO queryArticleDetail(Long articleId);

    PageQueryResponse<QueryRecommendedArticleListItemResponseVO> queryRecommendedArticleList(Integer pageIndex, Integer pageSize);
}
