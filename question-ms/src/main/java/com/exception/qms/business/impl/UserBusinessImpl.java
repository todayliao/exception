package com.exception.qms.business.impl;

import com.exception.qms.business.UserBusiness;
import com.exception.qms.domain.enhancement.UserAnswerContributionStatistics;
import com.exception.qms.domain.enhancement.UserArticleContributionStatistics;
import com.exception.qms.domain.enhancement.UserQuestionContributionStatistics;
import com.exception.qms.domain.entity.*;
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
import org.springframework.util.StringUtils;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

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
public class UserBusinessImpl implements UserBusiness {

    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionTagService questionTagService;
    @Autowired
    private Mapper mapper;

    @Override
    public PageQueryResponse<QueryUserPageListResponseVO> queryUserPageList(Integer pageIndex, Integer pageSize, String tab) {
        int totalCount = userService.queryUserPageListCount();

        List<QueryUserPageListResponseVO> userPageListResponseVOS = null;
        if (totalCount > 0) {

            List<User> users = userService.queryUserPageList(pageIndex, pageSize);

            userPageListResponseVOS = users.stream().map(user -> {
                QueryUserPageListResponseVO queryUserPageListResponseVO = new QueryUserPageListResponseVO();
                queryUserPageListResponseVO.setUserId(user.getId());
                queryUserPageListResponseVO.setUserName(user.getName());
                queryUserPageListResponseVO.setUserAvatar(user.getAvatar());
                queryUserPageListResponseVO.setUserIntroduction(user.getIntroduction());
                return queryUserPageListResponseVO;
            }).collect(Collectors.toList());
        }

        return new PageQueryResponse<QueryUserPageListResponseVO>().
                successPage(userPageListResponseVOS, pageIndex, totalCount, pageSize);
    }

    @Override
    public BaseResponse queryContributionData(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
        LocalDate lastYearToday = LocalDate.now().minus(1, ChronoUnit.YEARS);
        LocalDate lastYearYesterday = lastYearToday.minus(1, ChronoUnit.DAYS);
        // 问题
        List<UserQuestionContribution> userQuestionContributions = userService.queryUserQuestionContribution(userId, tomorrow, lastYearYesterday);

        List<UserQuestionContributionStatistics> userQuestionContributionStatisticsList
                = userQuestionContributions.stream()
                    .map(userQuestionContribution -> {
                        UserQuestionContributionStatistics userQuestionContributionStatistics = mapper.map(userQuestionContribution, UserQuestionContributionStatistics.class);
                        userQuestionContributionStatistics.setContributeDate(userQuestionContribution.getCreateTime().toLocalDate());
                        return userQuestionContributionStatistics;
                    }).collect(Collectors.toList());

        Map<LocalDate, Long> userQuestionContributionMap = userQuestionContributionStatisticsList.stream()
                .collect(Collectors.groupingBy(UserQuestionContributionStatistics::getContributeDate, Collectors.counting()));

        // 方案
        List<UserAnswerContribution> userAnswerContributions = userService.queryUserAnswerContribution(userId, tomorrow, lastYearYesterday);

        List<UserAnswerContributionStatistics> userAnswerContributionStatisticsList = userAnswerContributions.stream()
                .map(userAnswerContribution -> {
                    UserAnswerContributionStatistics userAnswerContributionStatistics = mapper.map(userAnswerContribution, UserAnswerContributionStatistics.class);
                    userAnswerContributionStatistics.setContributeDate(userAnswerContribution.getCreateTime().toLocalDate());
                    return userAnswerContributionStatistics;
                }).collect(Collectors.toList());

        Map<LocalDate, Long> userAnswerContributionMap = userAnswerContributionStatisticsList.stream()
                .collect(Collectors.groupingBy(UserAnswerContributionStatistics::getContributeDate, Collectors.counting()));

        // 文章
        List<UserArticleContribution> userArticleContributions = userService.queryUserArticleContribution(userId, tomorrow, lastYearYesterday);
        List<UserArticleContributionStatistics> userArticleContributionStatisticsList = userArticleContributions.stream()
                .map(userArticleContribution -> {
                    UserArticleContributionStatistics userArticleContributionStatistics = mapper.map(userArticleContribution, UserArticleContributionStatistics.class);
                    userArticleContributionStatistics.setContributeDate(userArticleContribution.getCreateTime().toLocalDate());
                    return userArticleContributionStatistics;
                }).collect(Collectors.toList());

        Map<LocalDate, Long> userArticleContributionMap = userArticleContributionStatisticsList.stream()
                .collect(Collectors.groupingBy(UserArticleContributionStatistics::getContributeDate, Collectors.counting()));


        QueryContributionDataResponseDTO queryContributionDataResponseDTO = new QueryContributionDataResponseDTO();


        LocalDate date = null;
        List<QueryContributionDataItemDTO> queryContributionDataItemDTOS = Lists.newArrayList();
        for (int i = 0; i < 370; i++) {
            QueryContributionDataItemDTO contributionItem = new QueryContributionDataItemDTO();

            date = (i == 0 ? today : date.minus(1, ChronoUnit.DAYS));

            contributionItem.setDate(date);

            int contributionCount = 0;
            if (!CollectionUtils.isEmpty(userQuestionContributionMap)
                    && userQuestionContributionMap.get(date) != null) {
                contributionCount += userQuestionContributionMap.get(date);
            }

            if (!CollectionUtils.isEmpty(userAnswerContributionMap)
                    && userAnswerContributionMap.get(date) != null) {
                contributionCount += userAnswerContributionMap.get(date);
            }

            if (!CollectionUtils.isEmpty(userArticleContributionMap)
                    && userArticleContributionMap.get(date) != null) {
                contributionCount += userArticleContributionMap.get(date);
            }

            contributionItem.setCount(contributionCount);
            queryContributionDataItemDTOS.add(contributionItem);
        }
        queryContributionDataResponseDTO.setContributionItems(queryContributionDataItemDTOS);

        // 统计此用户本月的贡献总数
        int totalCountOfMonth = 0;
        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

        for (Map.Entry<LocalDate, Long> entry : userQuestionContributionMap.entrySet()) {
            if (entry.getKey().isEqual(firstDayOfMonth)
                    || entry.getKey().isAfter(firstDayOfMonth)) {
                totalCountOfMonth += entry.getValue();
            }
        }

        for (Map.Entry<LocalDate, Long> entry : userAnswerContributionMap.entrySet()) {
            if (entry.getKey().isEqual(firstDayOfMonth)
                    || entry.getKey().isAfter(firstDayOfMonth)) {
                totalCountOfMonth += entry.getValue();
            }
        }

        queryContributionDataResponseDTO.setTotalCountOfMonth(totalCountOfMonth);
        return new BaseResponse().success(queryContributionDataResponseDTO);
    }

    /**
     * 查询用户展示页数据
     *
     * @param userId
     * @return
     */
    @Override
    public BaseResponse queryUserDetail(Long userId, String tab) {
        List<User> users = userService.queryUsersByUserIds(Arrays.asList(userId));
        User currentUser = users.get(0);

        QueryUserDetailResponseVO queryUserDetailResponseVO = new QueryUserDetailResponseVO();
        // 用户信息
        queryUserDetailResponseVO.setUserId(currentUser.getId());
        queryUserDetailResponseVO.setUserName(currentUser.getName());
        queryUserDetailResponseVO.setUserIntroduction(currentUser.getIntroduction());
        queryUserDetailResponseVO.setUserAvatar(currentUser.getAvatar());

        boolean isUserDetailInfoAllNull = StringUtils.isEmpty(currentUser.getEmail())
                && StringUtils.isEmpty(currentUser.getGithubUrl())
                && StringUtils.isEmpty(currentUser.getLinkUrl())
                && StringUtils.isEmpty(currentUser.getLocation());
        queryUserDetailResponseVO.setIsUserDetailInfoAllNull(isUserDetailInfoAllNull);
        queryUserDetailResponseVO.setEmail(StringUtils.isEmpty(currentUser.getEmail()) ? null : currentUser.getEmail());
        queryUserDetailResponseVO.setGithubUrl(StringUtils.isEmpty(currentUser.getGithubUrl()) ? null : currentUser.getGithubUrl());
        queryUserDetailResponseVO.setLinkUrl(StringUtils.isEmpty(currentUser.getLinkUrl()) ? null : currentUser.getLinkUrl());
        queryUserDetailResponseVO.setLocation(StringUtils.isEmpty(currentUser.getLocation()) ? null : currentUser.getLocation());

        // 用户维护的问题
        int totalQuestionCount = questionService.queryQuestionTotalCountByUser(userId);

        if (totalQuestionCount > 0) {
            List<Question> questionList = questionService.queryQuestionPageListByUser(userId);

            // 关联相关标签信息
            // 获取问题 ids
            List<Long> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
            Map<Long, List<TagResponseVO>> questionIdTagIdsMap = questionTagService.queryTagInfoByQuestionIds(questionIds);

            List<QueryUserDetailQuestionItemResponseVO> queryUserDetailQuestionItemResponseVOS = null;
            if (!CollectionUtils.isEmpty(questionIdTagIdsMap)) {
                queryUserDetailQuestionItemResponseVOS = questionList.stream().map(question -> {
                    QueryUserDetailQuestionItemResponseVO queryUserDetailQuestionItemResponseVO = mapper.map(question, QueryUserDetailQuestionItemResponseVO.class);
                    queryUserDetailQuestionItemResponseVO.setBeforeTimeStr(TimeUtil.calculateTimeDifference(question.getCreateTime()));
                    queryUserDetailQuestionItemResponseVO.setTags(questionIdTagIdsMap.get(question.getId()));
                    return queryUserDetailQuestionItemResponseVO;
                }).collect(Collectors.toList());
            }
            queryUserDetailResponseVO.setQuestions(queryUserDetailQuestionItemResponseVOS);
        }

        queryUserDetailResponseVO.setTotalQuestionCount(totalQuestionCount);
        return new BaseResponse().success(queryUserDetailResponseVO);
    }

}
