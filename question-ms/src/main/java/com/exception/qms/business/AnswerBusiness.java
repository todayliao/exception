package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.web.form.answer.AnswerUpdateForm;
import com.exception.qms.web.vo.home.AnswerInfoResponseVO;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface AnswerBusiness {

    AnswerInfoResponseVO queryAnswerInfo(Long answerId);

    BaseResponse updateAnswer(AnswerUpdateForm answerUpdateForm);
}
