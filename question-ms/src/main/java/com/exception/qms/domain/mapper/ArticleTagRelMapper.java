package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.ArticleTagRel;

public interface ArticleTagRelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleTagRel record);

    int insertSelective(ArticleTagRel record);

    ArticleTagRel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleTagRel record);

    int updateByPrimaryKey(ArticleTagRel record);
}