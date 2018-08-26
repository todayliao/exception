package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.HealthArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HealthArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HealthArticle record);

    int insertSelective(HealthArticle record);

    HealthArticle selectByPrimaryKey(Long id);

    List<HealthArticle> queryHealthArticleList(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    int queryHealthArticleListCount();

    int updateByPrimaryKeySelective(HealthArticle record);

    int updateByPrimaryKey(HealthArticle record);
}