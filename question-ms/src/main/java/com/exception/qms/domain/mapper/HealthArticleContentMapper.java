package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.HealthArticleContent;

public interface HealthArticleContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HealthArticleContent record);

    int insertSelective(HealthArticleContent record);

    HealthArticleContent selectByArticleId(Long articleId);

    HealthArticleContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HealthArticleContent record);

    int updateByPrimaryKeyWithBLOBs(HealthArticleContent record);

    int updateByPrimaryKey(HealthArticleContent record);
}