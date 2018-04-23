package com.exception.qms.business.impl;

import com.exception.qms.business.AnswerBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.service.AnswerService;
import com.exception.qms.web.form.answer.AnswerUpdateForm;
import com.exception.qms.web.vo.home.AnswerInfoResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        answerDesc.setDescriptionCn(answerUpdateForm.getAnswerDesc());
        answerService.updateAnswerDesc(answerDesc);
        return new BaseResponse().success();
    }
}
