package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.Article;
import com.exception.qms.domain.entity.ArticleContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Long id);

    List<Article> queryHomeArticleList(@Param("limit") int limit, @Param("limitTime") String limitTime);

    List<Article> queryAll();

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);
}