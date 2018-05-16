package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.UserAnswerContribution;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserAnswerContributionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAnswerContribution record);

    int insertSelective(UserAnswerContribution record);

    UserAnswerContribution selectByPrimaryKey(Long id);

    List<UserAnswerContribution> queryUserAnswerContribution(@Param("userId") long userId,
                                                             @Param("today") LocalDate today,
                                                             @Param("lastYearToday") LocalDate lastYearToday);

    int updateByPrimaryKeySelective(UserAnswerContribution record);

    int updateByPrimaryKey(UserAnswerContribution record);
}