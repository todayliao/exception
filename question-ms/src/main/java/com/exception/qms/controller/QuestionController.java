package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.utils.SpringMVCUtil;
import com.exception.qms.web.dto.question.request.ChangeQuestionVoteUpRequestDTO;
import com.exception.qms.web.dto.question.request.QuestionViewNumIncreaseRequestDTO;
import com.exception.qms.web.form.question.QuestionForm;
import com.exception.qms.web.form.question.QuestionUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription qms controller
 **/
@Controller
public class QuestionController extends BaseController {

    @Autowired
    private QuestionBusiness questionBusiness;

    /**
     * 问题详情
     *
     * @param questionId
     * @return
     */
    @GetMapping("/question/{questionId}")
    public String queryQuestionInfo(@PathVariable("questionId") Long questionId, Model model, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(),
                questionBusiness.queryQuestionDetail(questionId, user == null ? null : user.getId()));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.QUESTION.getCode());
        return "question/question-detail";
    }

    /**
     * 问题添加页面展示
     *
     * @return
     */
    @GetMapping("/question")
    @OperatorLog(description = "问题添加页面展示")
    public String showQuestionAddPage(Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.QUESTION.getCode());
        return "question/question-add";
    }

    /**
     * 问题添加
     *
     * @return
     */
    @PostMapping("/question")
    @OperatorLog(description = "问题添加")
    @ResponseBody
    public BaseResponse addQuestion(@Validated QuestionForm questionForm, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return questionBusiness.addQuestion(questionForm, user == null ? null : user.getId());
    }

    /**
     * 问题改进页面展示
     *
     * @return
     */
    @GetMapping("/question/{questionId}/edit")
    @OperatorLog(description = "问题改进页面展示")
    public String showQuestionEditPage(@PathVariable("questionId") Long quesitonId, Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), questionBusiness.queryQuestionInfo(quesitonId));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.QUESTION.getCode());
        return "question/question-edit";
    }

    /**
     * 问题更新
     *
     * @param questionUpdateForm
     * @return
     */
    @PostMapping("/question/edit")
    @ResponseBody
    public BaseResponse updateQuestion(@Validated QuestionUpdateForm questionUpdateForm, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return questionBusiness.updateQuestion(questionUpdateForm, user.getId());
    }

    /**
     * 问题被浏览数增加
     *
     * @param questionViewNumIncreaseDTO
     * @return
     */
    @PostMapping("/question/viewNum/increase")
    @ResponseBody
    public BaseResponse increaseQuestionViewNum(@Validated @RequestBody QuestionViewNumIncreaseRequestDTO questionViewNumIncreaseDTO, HttpServletRequest request) {
        return questionBusiness.increaseQuestionViewNum(questionViewNumIncreaseDTO, request);
    }

    /**
     * 问题赞数改变（增加或减少）
     *
     * @return
     */
    @PostMapping("/api/question/voteUp/change")
    @ResponseBody
    public BaseResponse changeQuestionVoteUp(@Validated @RequestBody ChangeQuestionVoteUpRequestDTO changeQuestionVoteUpRequestDTO, HttpSession session) {
        User user = SpringMVCUtil.getCurrentLoginUser(session);
        return questionBusiness.changeQuestionVoteUp(changeQuestionVoteUpRequestDTO, user.getId());
    }
}
