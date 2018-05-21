package com.exception.qms.business.impl;

import com.exception.qms.business.AnswerBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.Answer;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.domain.entity.AnswerEditHistory;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.enums.UserAnswerContributionTypeEnum;
import com.exception.qms.enums.VoteOperationTypeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.AnswerService;
import com.exception.qms.service.AnswerVoteUserService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.StringUtil;
import com.exception.qms.web.dto.question.request.ChangeAnswerVoteUpRequestDTO;
import com.exception.qms.web.form.answer.AnswerUpdateForm;
import com.exception.qms.web.vo.home.AnswerInfoResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class AnswerBusinessImpl implements AnswerBusiness {

    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerVoteUserService answerVoteUserService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @Override
    public AnswerInfoResponseVO queryAnswerInfo(Long answerId) {
        AnswerDesc answerDesc = answerService.queryAnswerDescInfo(answerId);
        AnswerInfoResponseVO answerInfoResponseVO = mapper.map(answerDesc, AnswerInfoResponseVO.class);
        answerInfoResponseVO.setAnswerDesc(answerDesc.getDescriptionCn());
        return answerInfoResponseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateAnswer(AnswerUpdateForm answerUpdateForm, Long userId) {
        if (userId == null) {
            log.error("the userId is null");
            throw new QMSException(QmsResponseCodeEnum.USER_IS_NULL);
        }
        // 老的解决方案数据入历史表
        long answerId = answerUpdateForm.getId();

        AnswerDesc answerDescTmp = answerService.queryAnswerDescInfo(answerId);
        // 判断修改内容是否发生实质的改变
        boolean isDescNotChanged = answerDescTmp.getDescriptionCn().trim()
                .equals(answerUpdateForm.getAnswerDesc().trim());
        if (isDescNotChanged) {
            log.warn("the answer desc has't change, userId: {}, answerId:{}", userId, answerUpdateForm.getId());
            throw new QMSException(QmsResponseCodeEnum.UPDATE_CONTENT_NOT_CHANGE);
        }

        Answer answerTmp = answerService.queryAnswerInfo(answerId);
        AnswerEditHistory answerEditHistory = new AnswerEditHistory();
        answerEditHistory.setAnswerId(answerId);
        answerEditHistory.setAnswerCreateTime(answerTmp.getUpdateTime());
        // 设置历史数据的创建者
        if (answerTmp.getLatestEditorUserId() == 0) {
            answerEditHistory.setCreateUserId(answerTmp.getCreateUserId());
        } else {
            answerEditHistory.setCreateUserId(answerTmp.getLatestEditorUserId());
        }
        answerEditHistory.setDescriptionCn(answerDescTmp.getDescriptionCn());

        answerService.addAnswerEditHistory(answerEditHistory);

        Answer answer = new Answer();
        answer.setId(answerId);
        answer.setLatestEditorUserId(userId);
        answerService.updateAnswer(answer);

        AnswerDesc answerDesc = mapper.map(answerUpdateForm, AnswerDesc.class);
        answerDesc.setDescriptionCn(StringUtil.spacingText(answerUpdateForm.getAnswerDesc()));
        answerService.updateAnswerDesc(answerDesc);

        // 异步添加贡献数据
        executorService.execute(() -> userService.addAnswerContribution(answerId, userId, UserAnswerContributionTypeEnum.EDIT.getCode()));

        return new BaseResponse().success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse changeAnswerVoteUp(ChangeAnswerVoteUpRequestDTO changeAnswerVoteUpRequestDTO, Long userId) {
        int operationType = changeAnswerVoteUpRequestDTO.getOperationType();
        long answerId = changeAnswerVoteUpRequestDTO.getAnswerId();

        if (userId == null) {
            throw new QMSException(QmsResponseCodeEnum.USER_IS_NULL);
        }

        VoteOperationTypeEnum voteOperationTypeEnum = VoteOperationTypeEnum.codeOf(operationType);

        switch (voteOperationTypeEnum) {
            case UP:
                answerService.voteUpAnswer(answerId);
                // 添加问题和点赞用户的绑定信息
                try {
                    answerVoteUserService.addAnswerVoteUserRecord(answerId, operationType, userId);
                } catch (DuplicateKeyException e) {
                    log.error("duplicate key ==> answerId: {}, userId: {}, operationType: {}", answerId, userId, operationType);
                    throw new QMSException(QmsResponseCodeEnum.ALREADY_VOTE_UP);
                }
                break;
            case DOWN:
                answerService.voteDownAnswer(answerId);
                // 添加问题和踩用户的绑定信息
                try {
                    answerVoteUserService.addAnswerVoteUserRecord(answerId, operationType, userId);
                } catch (DuplicateKeyException e) {
                    log.error("duplicate key ==> answerId: {}, userId: {}, operationType: {}", answerId, userId, operationType);
                    throw new QMSException(QmsResponseCodeEnum.ALREADY_VOTE_DOWN);
                }
                break;
            default:
                throw new QMSException(QmsResponseCodeEnum.PARAM_ERROR);
        }

        return new BaseResponse().success();
    }
}
