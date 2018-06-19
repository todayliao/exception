package com.exception.qms.service;

import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.QuestionDesc;
import com.exception.qms.domain.entity.QuestionEditHistory;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface QuestionService {
    /**
     * 查询首页问题列表
     * @return
     */
    List<Question> queryQuestionPageList(int pageIndex, int pageSize, String orderByColumn);

    List<Question> queryQuestionPageListByUser(long userId);

    /**
     * 查询首页问题列表总数
     * @return
     */
    int queryQuestionTotalCount();

    int queryQuestionTotalCountByUser(Long userId);

    int queryQuestionTagTotalCount(List<Long> questionIds);

    List<Question> queryQuestionTagPageList(List<Long> questionIds, Integer pageIndex, Integer pageSize, String orderByColumn);

    List<Question> queryHotQuestions();

    List<Question> queryAllQuestions();

    Question queryQuestionInfo(Long questionId);

    QuestionDesc queryQuestionDesc(Long questionId);

    int addQuestion(Question question);

    int addQuestionDesc(QuestionDesc questionDesc);

    int updateQuestion(Question question);

    int updateQuestionDesc(QuestionDesc questionDesc);

    int increaseQuestionViewNum(Long questionId);

    int voteUpQuestion(long questionId);

    int voteDownQuestion(long questionId);

    int addQuestionEditHistory(QuestionEditHistory questionEditHistory);
}
