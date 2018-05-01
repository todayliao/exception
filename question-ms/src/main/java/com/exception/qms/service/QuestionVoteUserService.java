package com.exception.qms.service;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface QuestionVoteUserService {

    int addQuestionVoteUserRecord(long questionId, int operationType, long userId);

    boolean isCurrentUserVoteUp(long userId, long questionId);
}
