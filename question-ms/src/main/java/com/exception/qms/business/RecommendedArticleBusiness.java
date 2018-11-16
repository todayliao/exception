package com.exception.qms.business;

import com.exception.qms.model.vo.article.QueryRecommendedArticleListItemResponseVO;
import com.exception.qms.model.vo.article.RecommendedArticleDetailResponseVO;
import site.exception.common.PageQueryResponse;

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
