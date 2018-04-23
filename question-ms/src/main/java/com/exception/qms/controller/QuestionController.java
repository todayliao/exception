package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.web.form.question.QuestionForm;
import com.exception.qms.web.form.question.QuestionUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
    @OperatorLog(description = "问题详情")
    public String queryQuestionInfo(@PathVariable("questionId") Long questionId, Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), questionBusiness.queryQuestionDetail(questionId));
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
    public String addQuestion(@Validated QuestionForm questionForm, HttpSession session) {
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        User user = (User) securityContext.getAuthentication().getPrincipal();
        questionBusiness.addQuestion(questionForm, user == null ? null : user.getId());
        return "redirect:/home";
    }

    /**
     * 问题改进页面展示
     *
     * @return
     */
    @GetMapping("/question/{questionId}/edit")
    @OperatorLog(description = "问题添加页面展示")
    public String showQuestionEditPage(@PathVariable("questionId") Long quesitonId, Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), questionBusiness.queryQuestionInfo(quesitonId));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.QUESTION.getCode());
        return "question/question-edit";
    }

    /**
     * 问题修改
     *
     * @return
     */
    @PostMapping("/question/edit")
    @OperatorLog(description = "问题更新")
    public String updateQuestion(@Validated QuestionUpdateForm questionUpdateForm) {
        questionBusiness.updateQuestion(questionUpdateForm);
        // 跳转问题展示页
        return String.format("redirect:/question/%d", questionUpdateForm.getId());
    }
}
