package com.exception.qms.business.impl;

import com.exception.qms.business.SEOBusiness;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.enhancement.UserAnswerContributionStatistics;
import com.exception.qms.domain.enhancement.UserQuestionContributionStatistics;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.entity.UserAnswerContribution;
import com.exception.qms.domain.entity.UserQuestionContribution;
import com.exception.qms.service.BaiduLinkPushService;
import com.exception.qms.service.QuestionService;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.TimeUtil;
import com.exception.qms.web.dto.user.response.QueryContributionDataItemDTO;
import com.exception.qms.web.dto.user.response.QueryContributionDataResponseDTO;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.user.QueryUserDetailQuestionItemResponseVO;
import com.exception.qms.web.vo.user.QueryUserDetailResponseVO;
import com.exception.qms.web.vo.user.QueryUserPageListResponseVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class SEOBusinessImpl implements SEOBusiness {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private BaiduLinkPushService baiduLinkPushService;

    @Override
    public BaseResponse pushAllQuestion() {
        List<Question> questions = questionService.queryAllQuestions();
        questions.parallelStream().forEach(question -> {
            baiduLinkPushService.pushQuestionDetailPageLink(question.getId());
        });
        return new BaseResponse().success();
    }
}
