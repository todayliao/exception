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

    List<Question> queryHomeQuestionList(@Param("limit") int limit,
                                         @Param("limitTime") String limitTime);

    List<Question> queryQuestionPageListByUser(long userId);

    int queryQuestionTotalCount();

    int queryQuestionTotalCountByUser(Long userId);

    int queryQuestionTagTotalCount(List<Long> questionIds);

    List<Question> queryQuestionTagPageList(@Param("questionIds") List<Long> questionIds,
                                            @Param("start") int start,
                                            @Param("pageSize") int pageSize,
                                            @Param("orderByColumn") String orderByColumn);

    List<Question> queryHotQuestions();

    List<Question> queryAllQuestions();


    Question queryQuestionInfo(Long questionId);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    int increaseQuestionViewNum(Long questionId);

    int voteUpQuestion(long questionId);

    int voteDownQuestion(long questionId);

    List<Question> queryAll();
}