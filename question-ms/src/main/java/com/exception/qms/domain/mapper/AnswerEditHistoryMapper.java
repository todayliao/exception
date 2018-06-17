package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.AnswerEditHistory;
import org.apache.ibatis.annotations.Param;

public interface AnswerEditHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AnswerEditHistory record);

    int insertSelective(AnswerEditHistory record);

    AnswerEditHistory selectByPrimaryKey(Long id);

    AnswerEditHistory queryLatestRecordByAnswerIdAndUserId(@Param("answerId") Long answerId, @Param("userId") Long userId);

    int updateByPrimaryKeySelective(AnswerEditHistory record);

    int updateByPrimaryKeyWithBLOBs(AnswerEditHistory record);

    int updateByPrimaryKey(AnswerEditHistory record);
}