package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.AnswerDesc;

import java.util.List;

public interface AnswerDescMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AnswerDesc record);

    int insertSelective(AnswerDesc record);

    AnswerDesc selectByPrimaryKey(Long id);

    AnswerDesc queryAnswerDescInfo(Long answerId);

    List<AnswerDesc> queryByAnswerIds(List<Long> answerIds);

    int updateByPrimaryKeySelective(AnswerDesc record);

    int updateByPrimaryKeyWithBLOBs(AnswerDesc record);

    int updateByPrimaryKey(AnswerDesc record);
}