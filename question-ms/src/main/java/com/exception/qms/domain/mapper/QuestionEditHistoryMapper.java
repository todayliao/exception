package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.QuestionEditHistory;
import org.apache.ibatis.annotations.Param;

public interface QuestionEditHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionEditHistory record);

    int insertSelective(QuestionEditHistory record);

    QuestionEditHistory selectByPrimaryKey(Long id);

    QuestionEditHistory queryLatestRecordByQuestionIdAndUserId(@Param("questionId") Long questionId, @Param("userId") Long userId);

    int updateByPrimaryKeySelective(QuestionEditHistory record);

    int updateByPrimaryKeyWithBLOBs(QuestionEditHistory record);

    int updateByPrimaryKey(QuestionEditHistory record);
}