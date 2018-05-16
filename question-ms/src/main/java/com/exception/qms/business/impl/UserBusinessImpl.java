package com.exception.qms.business.impl;

import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.enhancement.UserAnswerContributionStatistics;
import com.exception.qms.domain.enhancement.UserQuestionContributionStatistics;
import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.entity.UserAnswerContribution;
import com.exception.qms.domain.entity.UserQuestionContribution;
import com.exception.qms.service.UserService;
import com.exception.qms.web.dto.user.response.QueryContributionDataItemDTO;
import com.exception.qms.web.dto.user.response.QueryContributionDataResponseDTO;
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
        LocalDate lastYearToday = LocalDate.now().minus(1, ChronoUnit.YEARS);
        List<UserQuestionContribution> userQuestionContributions = userService.queryUserQuestionContribution(userId, today, lastYearToday);

        List<UserQuestionContributionStatistics> userQuestionContributionStatisticsList
                = userQuestionContributions.stream()
                    .map(userQuestionContribution -> {
                        UserQuestionContributionStatistics userQuestionContributionStatistics = mapper.map(userQuestionContribution, UserQuestionContributionStatistics.class);
                        userQuestionContributionStatistics.setContributeDate(userQuestionContribution.getCreateTime().toLocalDate());
                        return userQuestionContributionStatistics;
                    }).collect(Collectors.toList());

        Map<LocalDate, Long> userQuestionContributionMap = userQuestionContributionStatisticsList.stream().collect(Collectors.groupingBy(UserQuestionContributionStatistics::getContributeDate, Collectors.counting()));

        List<UserAnswerContribution> userAnswerContributions = userService.queryUserAnswerContribution(userId, today, lastYearToday);

        List<UserAnswerContributionStatistics> userAnswerContributionStatisticsList = userAnswerContributions.stream()
                .map(userAnswerContribution -> {
                    UserAnswerContributionStatistics userAnswerContributionStatistics = mapper.map(userAnswerContribution, UserAnswerContributionStatistics.class);
                    userAnswerContributionStatistics.setContributeDate(userAnswerContribution.getCreateTime().toLocalDate());
                    return userAnswerContributionStatistics;
                }).collect(Collectors.toList());

        QueryContributionDataResponseDTO queryContributionDataResponseDTO = new QueryContributionDataResponseDTO();

        Map<LocalDate, Long> userAnswerContributionMap = userAnswerContributionStatisticsList.stream().collect(Collectors.groupingBy(UserAnswerContributionStatistics::getContributeDate, Collectors.counting()));
        LocalDate date = null;
        List<QueryContributionDataItemDTO> queryContributionDataItemDTOS = Lists.newArrayList();
        for (int i = 0; i < 370; i++) {
            QueryContributionDataItemDTO contributionItem = new QueryContributionDataItemDTO();

            date = (i == 0 ? today.minus(1, ChronoUnit.DAYS) : date.minus(1, ChronoUnit.DAYS));

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

}
