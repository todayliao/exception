package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.Answer;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.UserAnswerContribution;
import com.exception.qms.domain.entity.UserQuestionContribution;
import com.exception.qms.domain.mapper.AnswerMapper;
import com.exception.qms.domain.mapper.QuestionMapper;
import com.exception.qms.domain.mapper.UserAnswerContributionMapper;
import com.exception.qms.domain.mapper.UserQuestionContributionMapper;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.enums.UserAnswerContributionTypeEnum;
import com.exception.qms.enums.UserQuestionContributionTypeEnum;
import com.exception.qms.web.dto.user.response.QueryContributionDataResponseDTO;
import com.google.common.collect.Lists;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Random;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class UserController extends BaseController {

    @Autowired
    private UserBusiness userBusiness;

    /**
     * 登录页
     *
     * @return
     */
    @GetMapping("/user/login")
    @OperatorLog(description = "登录页")
    public String showUserLoginPage(Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        return "user/user-login";
    }

    @GetMapping("/user")
    @OperatorLog(description = "展示用户墙")
    public String showUserWall(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "pageSize", defaultValue = "36") Integer pageSize,
                               @RequestParam(value = "tab", defaultValue = "new") String tab,
                               Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), userBusiness.queryUserPageList(pageIndex, pageSize, tab));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        return "user/user-wall";
    }

    @GetMapping("/user/{userId}")
    @OperatorLog(description = "展示用户")
    public String showUser(@PathVariable("userId") Long userId,
                           @RequestParam(value = "tab", defaultValue = "question") String tab,
                           Model model) {
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.USER.getCode());
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), userBusiness.queryUserDetail(userId, tab));
        model.addAttribute("userId", userId);
        return "user/user-detail";
    }


//    @Autowired
//    private QuestionMapper questionMapper;
//    @Autowired
//    private AnswerMapper answerMapper;
//    @Autowired
//    private UserQuestionContributionMapper userQuestionContributionMapper;
//    @Autowired
//    private UserAnswerContributionMapper userAnswerContributionMapper;
//
//
//    /**
//     * 待删除的 api
//     * @return
//     */
//    @GetMapping("/user/contribution/create")
//    @OperatorLog(description = "用户贡献数据生成")
//    @ResponseBody
//    public BaseResponse createUserContributionData() {
//        List<Question> questions = questionMapper.queryAll();
//        questions.forEach(question -> {
//            UserQuestionContribution userQuestionContribution = new UserQuestionContribution();
//            userQuestionContribution.setCreateTime(question.getCreateTime());
//            userQuestionContribution.setQuestionId(question.getId());
//            userQuestionContribution.setUserId(question.getCreateUserId());
//            userQuestionContribution.setType(UserQuestionContributionTypeEnum.CREATE.getCode());
//            userQuestionContributionMapper.insert(userQuestionContribution);
//        });
//
//        List<Answer> answers = answerMapper.queryAll();
//        answers.forEach(answer -> {
//            UserAnswerContribution userAnswerContribution = new UserAnswerContribution();
//            userAnswerContribution.setAnswerId(answer.getId());
//            userAnswerContribution.setCreateTime(answer.getCreateTime());
//            userAnswerContribution.setUserId(answer.getCreateUserId());
//            userAnswerContribution.setType(UserAnswerContributionTypeEnum.CREATE.getCode());
//            userAnswerContributionMapper.insert(userAnswerContribution);
//        });
//        return new BaseResponse().success();
//    }

    @GetMapping("/user/{userId}/contribution/data")
    @OperatorLog(description = "用户贡献数据")
    @ResponseBody
    public BaseResponse queryContributionData(@PathVariable("userId") Long userId) {
        return userBusiness.queryContributionData(userId);
    }

}
