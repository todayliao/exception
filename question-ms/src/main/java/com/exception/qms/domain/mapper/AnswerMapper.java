package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.Answer;

import java.util.List;

public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Answer record);

    int insertSelective(Answer record);

    Answer selectByPrimaryKey(Long id);

    List<Answer> queryByQuestionId(Long questionId);

    int updateByPrimaryKeySelective(Answer record);

    int updateByPrimaryKey(Answer record);

    int voteUpAnswer(long answerId);

    int voteDownAnswer(long answerId);

    List<Answer> queryAll();
}