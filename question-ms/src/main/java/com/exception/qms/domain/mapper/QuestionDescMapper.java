package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.QuestionDesc;

import java.util.List;

public interface QuestionDescMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionDesc record);

    int insertSelective(QuestionDesc record);

    QuestionDesc selectByPrimaryKey(Long id);

    QuestionDesc queryQuestionDesc(Long questionId);

    int updateByPrimaryKeySelective(QuestionDesc record);

    int updateByPrimaryKeyWithBLOBs(QuestionDesc record);

    int updateByPrimaryKey(QuestionDesc record);

    int updateByQuestionId(QuestionDesc questionDesc);
}