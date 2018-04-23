package com.exception.qms.service;

import com.exception.qms.domain.entity.Answer;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.web.vo.home.AnswerResponseVO;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface AnswerService {

    List<AnswerResponseVO> queryAnswersByQuestionId(Long questionId);

    int addAnswer(Answer answer);

    int addAnswerDesc(AnswerDesc answerDesc);

    AnswerDesc queryAnswerDescInfo(Long answerId);

    int updateAnswerDesc(AnswerDesc answerDesc);
}
