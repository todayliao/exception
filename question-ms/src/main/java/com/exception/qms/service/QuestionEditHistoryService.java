package com.exception.qms.service;

import com.exception.qms.domain.entity.QuestionEditHistory;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface QuestionEditHistoryService {

    QuestionEditHistory query(Long questionId, Long userId);
}
