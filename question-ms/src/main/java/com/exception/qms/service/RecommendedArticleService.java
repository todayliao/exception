package com.exception.qms.service;

import com.exception.qms.domain.entity.*;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface RecommendedArticleService {

    int queryRecommendedArticleTotalCount();

    List<RecommendedArticle> queryRecommendedArticleList(Integer pageIndex, Integer pageSize);

    RecommendedArticle queryArticleDetail(Long articleId);

    RecommendedArticleContent queryArticleContent(Long articleId);

    List<RecommendedArticle> queryAllArticles();
}
