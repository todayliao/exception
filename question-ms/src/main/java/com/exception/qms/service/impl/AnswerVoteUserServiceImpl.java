package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.AnswerVoteUserRel;
import com.exception.qms.domain.entity.QuestionVoteUserRel;
import com.exception.qms.domain.mapper.AnswerVoteUserRelMapper;
import com.exception.qms.domain.mapper.QuestionVoteUserRelMapper;
import com.exception.qms.service.AnswerVoteUserService;
import com.exception.qms.service.QuestionVoteUserService;
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
public class AnswerVoteUserServiceImpl implements AnswerVoteUserService {

    @Autowired
    private AnswerVoteUserRelMapper answerVoteUserRelMapper;


    @Override
    public int addAnswerVoteUserRecord(long answerId, int operationType, Long userId) {
        // delete the vote down record at first
        answerVoteUserRelMapper.deleteVoteDownRecord(answerId, userId, -operationType);
        AnswerVoteUserRel answerVoteUserRel = new AnswerVoteUserRel();
        answerVoteUserRel.setAnswerId(answerId);
        answerVoteUserRel.setVoteUserId(userId);
        answerVoteUserRel.setVoteOperationType(operationType);
        return answerVoteUserRelMapper.insert(answerVoteUserRel);
    }
}
