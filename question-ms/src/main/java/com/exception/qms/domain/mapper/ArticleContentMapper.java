package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.ArticleContent;

public interface ArticleContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleContent record);

    int insertSelective(ArticleContent record);

    ArticleContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleContent record);

    int updateByPrimaryKeyWithBLOBs(ArticleContent record);

    int updateByPrimaryKey(ArticleContent record);
}