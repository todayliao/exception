package com.exception.qms.service;

import com.exception.qms.domain.entity.Answer;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.domain.entity.AnswerEditHistory;
import com.exception.qms.web.vo.home.AnswerResponseVO;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface AnswerService {

    List<AnswerResponseVO> queryAnswersByQuestionId(Long questionId, Long userId);

    int addAnswer(Answer answer);

    int addAnswerDesc(AnswerDesc answerDesc);

    Answer queryAnswerInfo(long answerId);

    AnswerDesc queryAnswerDescInfo(Long answerId);

    List<Answer> queryByQuestionIds(List<Long> questionIds);

    List<AnswerDesc> queryDescByAnswerIds(List<Long> answerIds);

    List<Answer> queryMaxVoteAnswerIdsByQuestionIds(List<Long> questionIds);

    int updateAnswer(Answer answer);

    int updateAnswerDesc(AnswerDesc answerDesc);

    int voteUpAnswer(long answerId);

    int voteDownAnswer(long answerId);

    int addAnswerEditHistory(AnswerEditHistory answerEditHistory);
}
