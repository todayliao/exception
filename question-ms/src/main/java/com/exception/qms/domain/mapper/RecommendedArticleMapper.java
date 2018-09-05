package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.RecommendedArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecommendedArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RecommendedArticle record);

    int insertSelective(RecommendedArticle record);

    RecommendedArticle selectByPrimaryKey(Long id);

    int queryRecommendedArticleTotalCount();

    List<RecommendedArticle> queryRecommendedArticleList(@Param("start") int start,
                                                         @Param("pageSize") Integer pageSize);

    List<RecommendedArticle> queryAllArticles();

    int updateByPrimaryKeySelective(RecommendedArticle record);

    int updateByPrimaryKey(RecommendedArticle record);
}