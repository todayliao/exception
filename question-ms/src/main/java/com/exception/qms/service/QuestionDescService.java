package com.exception.qms.service;

import com.exception.qms.domain.entity.QuestionDesc;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface QuestionDescService {

    QuestionDesc queryQuestionDescByQuestionId(Long questionId);

}
