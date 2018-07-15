package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.ArticleContent;

import java.util.List;

public interface ArticleContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleContent record);

    int insertSelective(ArticleContent record);

    ArticleContent selectByPrimaryKey(Long id);

    ArticleContent selectByArticleId(Long articleId);

    List<ArticleContent> queryArticleContents(List<Long> articleIds);

    int updateByPrimaryKeySelective(ArticleContent record);

    int updateByPrimaryKeyWithBLOBs(ArticleContent record);

    int updateByPrimaryKey(ArticleContent record);
}