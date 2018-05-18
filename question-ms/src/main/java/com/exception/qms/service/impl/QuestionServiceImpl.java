package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.QuestionDesc;
import com.exception.qms.domain.entity.QuestionEditHistory;
import com.exception.qms.domain.mapper.QuestionDescMapper;
import com.exception.qms.domain.mapper.QuestionEditHistoryMapper;
import com.exception.qms.domain.mapper.QuestionMapper;
import com.exception.qms.service.QuestionService;
import com.exception.qms.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionDescMapper questionDescMapper;
    @Autowired
    private QuestionEditHistoryMapper questionEditHistoryMapper;

    @Override
    public List<Question> queryQuestionPageList(int pageIndex, int pageSize, String orderByColumn) {
        return questionMapper.queryQuestionPageList(PageUtil.calculateLimitSelectSqlStart(pageIndex, pageSize), pageSize, orderByColumn);
    }

    @Override
    public List<Question> queryQuestionPageListByUser(long userId) {
        return questionMapper.queryQuestionPageListByUser(userId);
    }

    @Override
    public int queryQuestionTotalCount() {
        return questionMapper.queryQuestionTotalCount();
    }

    @Override
    public int queryQuestionTotalCountByUser(Long userId) {
        return questionMapper.queryQuestionTotalCountByUser(userId);
    }

    @Override
    public List<Question> queryHotQuestions() {
        return questionMapper.queryHotQuestions();
    }

    @Override
    public Question queryQuestionInfo(Long questionId) {
        return questionMapper.queryQuestionInfo(questionId);
    }

    @Override
    public QuestionDesc queryQuestionDesc(Long questionId) {
        return questionDescMapper.queryQuestionDesc(questionId);
    }

    @Override
    public int addQuestion(Question question) {
        return questionMapper.insert(question);
    }

    @Override
    public int addQuestionDesc(QuestionDesc questionDesc) {
        return questionDescMapper.insert(questionDesc);
    }

    @Override
    public int updateQuestion(Question question) {
        return questionMapper.updateByPrimaryKeySelective(question);
    }

    @Override
    public int updateQuestionDesc(QuestionDesc questionDesc) {
        return questionDescMapper.updateByQuestionId(questionDesc);
    }

    @Override
    public int increaseQuestionViewNum(Long questionId) {
        return questionMapper.increaseQuestionViewNum(questionId);
    }

    @Override
    public int voteUpQuestion(long questionId) {
        return questionMapper.voteUpQuestion(questionId);
    }

    @Override
    public int voteDownQuestion(long questionId) {
        return questionMapper.voteDownQuestion(questionId);
    }

    @Override
    public int addQuestionEditHistory(QuestionEditHistory questionEditHistory) {
        return questionEditHistoryMapper.insert(questionEditHistory);
    }
}
