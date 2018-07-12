package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.ArticleTagRel;

import java.util.List;

public interface ArticleTagRelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleTagRel record);

    int insertSelective(ArticleTagRel record);

    void batchAddArticleTagRel(List<ArticleTagRel> articleTagRels);

    ArticleTagRel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleTagRel record);

    int updateByPrimaryKey(ArticleTagRel record);
}