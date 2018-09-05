package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.service.ArticleService;
import com.exception.qms.service.RecommendedArticleService;
import com.exception.qms.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class RecommendedArticleServiceImpl implements RecommendedArticleService {

    @Autowired
    private RecommendedArticleMapper recommendedArticleMapper;
    @Autowired
    private RecommendedArticleContentMapper recommendedArticleContentMapper;

    @Override
    public int queryRecommendedArticleTotalCount() {
        return recommendedArticleMapper.queryRecommendedArticleTotalCount();
    }

    @Override
    public List<RecommendedArticle> queryRecommendedArticleList(Integer pageIndex, Integer pageSize) {
        return recommendedArticleMapper.queryRecommendedArticleList(PageUtil.calculateLimitSelectSqlStart(pageIndex, pageSize), pageSize);
    }

    @Override
    public RecommendedArticle queryArticleDetail(Long articleId) {
        return recommendedArticleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    public RecommendedArticleContent queryArticleContent(Long articleId) {
        return recommendedArticleContentMapper.queryArticleContentByArticleId(articleId);
    }

    @Override
    public List<RecommendedArticle> queryAllArticles() {
        return recommendedArticleMapper.queryAllArticles();
    }
}
