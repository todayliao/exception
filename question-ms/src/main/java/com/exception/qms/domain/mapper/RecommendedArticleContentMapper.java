package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.RecommendedArticleContent;

public interface RecommendedArticleContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RecommendedArticleContent record);

    int insertSelective(RecommendedArticleContent record);

    RecommendedArticleContent selectByPrimaryKey(Long id);

    RecommendedArticleContent queryArticleContentByArticleId(Long articleId);

    int updateByPrimaryKeySelective(RecommendedArticleContent record);

    int updateByPrimaryKeyWithBLOBs(RecommendedArticleContent record);

    int updateByPrimaryKey(RecommendedArticleContent record);
}