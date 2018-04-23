package com.exception.qms.task;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/1
 * @time 下午4:28
 * @discription
 **/
@Component
@Slf4j
public class DbDataConvertTask {

//    @Autowired
//    private QuestionService questionService;
//    @Autowired
//    private QmsQuestionMapper qmsQuestionMapper;
//    @Autowired
//    private QmsQuestionDescMapper qmsQuestionDescMapper;
//    @Autowired
//    private SmsQuestionMapper smsQuestionMapper;
//    @Autowired
//    private QmsAnswerMapper qmsAnswerMapper;
//    @Autowired
//    private QmsAnswerDescMapper qmsAnswerDescMapper;
//    @Autowired
//    private QmsTagMapper qmsTagMapper;
//    @Autowired
//    private Mapper dozerMapper;
//
//    @Transactional(rollbackFor = Exception.class)
//    public void questionConvertStart(Question question) {
//        log.info("insert start ==>");
//        if (question == null) {
//            log.warn("question is null ...");
//            return;
//        }
//
//        QmsQuestion qmsQuestion = dozerMapper.map(question, QmsQuestion.class);;
//        qmsQuestion.setType(question.getStatus());
//        if (qmsQuestion.getVoteDown() == null) {
//            qmsQuestion.setVoteDown(0);
//        }
//        qmsQuestionMapper.insert(qmsQuestion);
//
//        SmsQuestion smsQuestion = dozerMapper.map(question, SmsQuestion.class);
//        smsQuestion.setIsTranslated(StringUtils.isNotBlank(question.getTitleCn()));
//        smsQuestion.setTitleEn(question.getTitle());
//        if (smsQuestion.getVoteDown() == null) {
//            smsQuestion.setVoteDown(0);
//        }
//        smsQuestionMapper.insert(smsQuestion);
//
//        log.info("insert success ==> questionId: {}", question.getId());
//    }
//
//    public void questionDescConvertStart(QuestionDescWithBLOBs questionDescWithBLOBs) {
//        log.info("insert start ==>");
//        if (questionDescWithBLOBs == null) {
//            log.warn("questionDescWithBLOBs is null ...");
//            return;
//        }
//
//        QmsQuestionDesc qmsQuestionDesc = dozerMapper.map(questionDescWithBLOBs, QmsQuestionDesc.class);;
//        qmsQuestionDescMapper.insert(qmsQuestionDesc);
//
//        log.info("insert success ==> questionId: {}", questionDescWithBLOBs.getId());
//
//    }
//
//    public void answerConvertStart(Answer answer) {
//        log.info("insert start ==>");
//        if (answer == null) {
//            log.warn("answer is null ...");
//            return;
//        }
//
//        QmsAnswer qmsAnswer = dozerMapper.map(answer, QmsAnswer.class);;
//        qmsAnswer.setVoteDown(0);
//        qmsAnswer.setVoteUp(0);
//        if (answer.getVoteCount() > 0) {
//            qmsAnswer.setVoteUp(answer.getVoteCount());
//        } else if (answer.getVoteCount() < 0) {
//            qmsAnswer.setVoteDown(answer.getVoteCount()*(-1));
//        }
//        qmsAnswerMapper.insert(qmsAnswer);
//
//        log.info("insert success ==> answerId: {}", answer.getId());
//    }
//
//    public void answerDescConvertStart(AnswerDesc answerDesc) {
//        log.info("insert start ==>");
//        if (answerDesc == null) {
//            log.warn("answerDesc is null ...");
//            return;
//        }
//
//        QmsAnswerDesc qmsAnswerDesc = dozerMapper.map(answerDesc, QmsAnswerDesc.class);
//        if (qmsAnswerDesc.getCreateUserId() == null) {
//            qmsAnswerDesc.setCreateUserId(1L);
//        }
//
//        qmsAnswerDescMapper.insert(qmsAnswerDesc);
//
//        log.info("insert success ==>");
//    }
//
//    public void tagConvertStart(Tag tag) {
//        log.info("insert start ==>");
//        if (tag == null) {
//            log.warn("tag is null ...");
//            return;
//        }
//
//        QmsTag qmsTag = dozerMapper.map(tag, QmsTag.class);
//        qmsTagMapper.insert(qmsTag);
//
//        log.info("insert success ==>");
//
//    }
}
