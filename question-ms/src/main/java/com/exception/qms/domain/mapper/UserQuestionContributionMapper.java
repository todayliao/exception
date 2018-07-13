package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.UserQuestionContribution;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserQuestionContributionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserQuestionContribution record);

    int insertSelective(UserQuestionContribution record);

    UserQuestionContribution selectByPrimaryKey(Long id);

    List<UserQuestionContribution> queryUserQuestionContribution(@Param("userId") long userId,
                                                                 @Param("tomorrow") LocalDate tomorrow,
                                                                 @Param("lastYearToday") LocalDate lastYearToday);

    int updateByPrimaryKeySelective(UserQuestionContribution record);

    int updateByPrimaryKey(UserQuestionContribution record);
}