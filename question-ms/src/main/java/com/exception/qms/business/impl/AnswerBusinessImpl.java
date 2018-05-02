package com.exception.qms.business.impl;

import com.exception.qms.business.AnswerBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.enums.VoteOperationTypeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.AnswerService;
import com.exception.qms.service.AnswerVoteUserService;
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
    private Mapper mapper;

    @Override
    public AnswerInfoResponseVO queryAnswerInfo(Long answerId) {
        AnswerDesc answerDesc = answerService.queryAnswerDescInfo(answerId);
        AnswerInfoResponseVO answerInfoResponseVO = mapper.map(answerDesc, AnswerInfoResponseVO.class);
        answerInfoResponseVO.setAnswerDesc(answerDesc.getDescriptionCn());
        return answerInfoResponseVO;
    }

    @Override
    public BaseResponse updateAnswer(AnswerUpdateForm answerUpdateForm) {
        AnswerDesc answerDesc = mapper.map(answerUpdateForm, AnswerDesc.class);
        answerDesc.setDescriptionCn(StringUtil.spacingText(answerUpdateForm.getAnswerDesc()));
        answerService.updateAnswerDesc(answerDesc);
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
