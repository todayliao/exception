package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.AnswerBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.utils.SpringMvcUtil;
import com.exception.qms.web.dto.question.request.ChangeAnswerVoteUpRequestDTO;
import com.exception.qms.web.dto.question.request.ChangeQuestionVoteUpRequestDTO;
import com.exception.qms.web.form.answer.AnswerUpdateForm;
import com.exception.qms.web.form.question.QuestionUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class AnswerController extends BaseController {

    @Autowired
    private AnswerBusiness answerBusiness;

    /**
     * 答案改进页面展示
     *
     * @return
     */
    @GetMapping("/answer/{answerId}/edit")
    @OperatorLog(description = "方案添加页面展示")
    public String showAnswerEditPage(@PathVariable("answerId") Long answerId, Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), answerBusiness.queryAnswerInfo(answerId));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.QUESTION.getCode());
        return "answer/answer-edit";
    }

    /**
     * 解决方案修改
     *
     * @return
     */
    @PostMapping("/answer/edit")
    @OperatorLog(description = "方案更新")
    public String updateAnswer(@Validated AnswerUpdateForm answerUpdateForm) {
        answerBusiness.updateAnswer(answerUpdateForm);
        return "redirect:/home";
    }

    /**
     * 回答方案赞数改变（增加或减少）
     *
     * @return
     */
    @PostMapping("/api/answer/voteUp/change")
    @ResponseBody
    public BaseResponse changeAnswerVoteUp(@Validated @RequestBody ChangeAnswerVoteUpRequestDTO changeAnswerVoteUpRequestDTO, HttpSession session) {
        User user = SpringMvcUtil.getCurrentLoginUser(session);
        return answerBusiness.changeAnswerVoteUp(changeAnswerVoteUpRequestDTO, user.getId());
    }
}
