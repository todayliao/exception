package com.exception.qms.service;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface AnswerVoteUserService {

    int addAnswerVoteUserRecord(long answerId, int operationType, Long userId);
}
