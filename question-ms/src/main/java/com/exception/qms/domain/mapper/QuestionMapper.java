package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Long id);

    List<Question> queryQuestionPageList(@Param("start") int start,
                                         @Param("pageSize") int pageSize,
                                         @Param("orderByColumn") String orderByColumn);

    int queryQuestionTotalCount();

    List<Question> queryHotQuestions();


    Question queryQuestionInfo(Long questionId);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    int increaseQuestionViewNum(Long questionId);

    int voteUpQuestion(long questionId);

    int voteDownQuestion(long questionId);

    List<Question> queryAll();
}