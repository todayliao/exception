package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.QuestionEditHistory;

public interface QuestionEditHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionEditHistory record);

    int insertSelective(QuestionEditHistory record);

    QuestionEditHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(QuestionEditHistory record);

    int updateByPrimaryKeyWithBLOBs(QuestionEditHistory record);

    int updateByPrimaryKey(QuestionEditHistory record);
}