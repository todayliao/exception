package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.QuestionVoteUserRel;
import com.exception.qms.domain.mapper.QuestionVoteUserRelMapper;
import com.exception.qms.enums.VoteOperationTypeEnum;
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
public class QuestionVoteUserServiceImpl implements QuestionVoteUserService {

    @Autowired
    private QuestionVoteUserRelMapper questionVoteUserRelMapper;


    @Override
    public int addQuestionVoteUserRecord(long questionId, int operationType, long userId) {
        // delete the vote down record at first
        questionVoteUserRelMapper.deleteVoteDownRecord(questionId, userId, -operationType);
        QuestionVoteUserRel questionVoteUserRel = new QuestionVoteUserRel();
        questionVoteUserRel.setQuestionId(questionId);
        questionVoteUserRel.setVoteUserId(userId);
        questionVoteUserRel.setVoteOperationType(operationType);
        return questionVoteUserRelMapper.insert(questionVoteUserRel);
    }

    @Override
    public boolean isCurrentUserVoteUp(long userId, long questionId) {
        int count = questionVoteUserRelMapper.queryCurrentUserVoteUpCount(userId, questionId, VoteOperationTypeEnum.UP.getCode());
        return count > 0;
    }
}
