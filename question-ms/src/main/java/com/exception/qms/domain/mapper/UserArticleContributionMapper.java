package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.UserArticleContribution;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserArticleContributionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserArticleContribution record);

    int insertSelective(UserArticleContribution record);

    UserArticleContribution selectByPrimaryKey(Long id);

    List<UserArticleContribution> queryUserArticleContribution(@Param("userId") long userId,
                                                               @Param("tomorrow") LocalDate tomorrow,
                                                               @Param("lastYearToday") LocalDate lastYearToday);

    int updateByPrimaryKeySelective(UserArticleContribution record);

    int updateByPrimaryKey(UserArticleContribution record);
}