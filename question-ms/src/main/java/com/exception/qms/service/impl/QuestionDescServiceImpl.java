package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.QuestionDesc;
import com.exception.qms.domain.mapper.QuestionDescMapper;
import com.exception.qms.service.QuestionDescService;
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
public class QuestionDescServiceImpl implements QuestionDescService {

    @Autowired
    private QuestionDescMapper questionDecMapper;

    @Override
    public QuestionDesc queryQuestionDescByQuestionId(Long questionId) {
//        return questionDecMapper.selectByQuestionId(questionId);
        return null;
    }
}
