package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.QuestionEditHistory;
import com.exception.qms.domain.mapper.QuestionEditHistoryMapper;
import com.exception.qms.service.QuestionEditHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class QuestionEditHistoryServiceImpl implements QuestionEditHistoryService {

    @Autowired
    private QuestionEditHistoryMapper questionEditHistoryMapper;

    @Override
    public QuestionEditHistory query(Long questionId, Long userId) {
        return questionEditHistoryMapper.queryLatestRecordByQuestionIdAndUserId(questionId, userId);
    }
}
