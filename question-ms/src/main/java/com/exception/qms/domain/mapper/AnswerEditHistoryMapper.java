package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.AnswerEditHistory;

public interface AnswerEditHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AnswerEditHistory record);

    int insertSelective(AnswerEditHistory record);

    AnswerEditHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AnswerEditHistory record);

    int updateByPrimaryKeyWithBLOBs(AnswerEditHistory record);

    int updateByPrimaryKey(AnswerEditHistory record);
}