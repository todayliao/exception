package com.exception.qms.business.impl;

import com.exception.qms.business.HomeBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.User;
import com.exception.qms.enums.QuestionTabEnum;
import com.exception.qms.service.AnswerService;
import com.exception.qms.service.QuestionService;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.TimeUtil;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.home.HomeHotTagResponseVO;
import com.exception.qms.web.vo.home.QueryHomeQuestionPageListResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription 首页
 **/
@Service
@Slf4j
public class HomeBusinessImpl implements HomeBusiness {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionTagService questionTagService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @Override
    public PageQueryResponse<QueryHomeQuestionPageListResponseVO> queryQuestionPageList(Integer pageIndex, Integer pageSize, String tab) {
        int totalCount = questionService.queryQuestionTotalCount();

        List<QueryHomeQuestionPageListResponseVO> homeQuestionPageListResponseVOS = null;

        if (totalCount > 0) {
            QuestionTabEnum questionTabEnum = QuestionTabEnum.codeOf(tab);

            String orderByColumn = null;
            // 根据 tab 获取需要排序的字段
            switch (questionTabEnum) {
                case NEW:
                    orderByColumn = "create_time";
                    break;
                case HOT:
                    orderByColumn = "view_num";
                    break;
                default:
                    break;
            }

            List<Question> questionList = questionService.queryQuestionPageList(pageIndex, pageSize, orderByColumn);

            // 关联相关标签信息
            // 获取问题 ids
            List<Long> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
            Map<Long, List<TagResponseVO>> questionIdTagIdsMap = questionTagService.queryTagInfoByQuestionIds(questionIds);

            // 用户信息
            List<Long> createUserIds = questionList.stream().map(Question::getCreateUserId).distinct().collect(Collectors.toList());
            List<User> users = userService.queryUsersByUserIds(createUserIds);
            Map<Long, User> userIdUserMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

            if (!CollectionUtils.isEmpty(questionIdTagIdsMap)) {
                homeQuestionPageListResponseVOS = questionList.stream().map(question -> {
                    QueryHomeQuestionPageListResponseVO homeQuestionPageListResponseVO = mapper.map(question, QueryHomeQuestionPageListResponseVO.class);
                    homeQuestionPageListResponseVO.setBeforeTimeStr(TimeUtil.calculateTimeDifference(question.getCreateTime()));
                    Long createUserId = question.getCreateUserId();

                    boolean isUserExist =
                            createUserId != null
                            && !CollectionUtils.isEmpty(userIdUserMap)
                            && userIdUserMap.get(createUserId) != null;

                    if (isUserExist) {
                        homeQuestionPageListResponseVO.setCreateUserName(userIdUserMap.get(createUserId).getName());
                        homeQuestionPageListResponseVO.setCreateUserAvatar(userIdUserMap.get(createUserId).getAvatar());
                    }
                    homeQuestionPageListResponseVO.setTags(questionIdTagIdsMap.get(question.getId()));
                    return homeQuestionPageListResponseVO;
                }).collect(Collectors.toList());
            }
        }

        return new PageQueryResponse<QueryHomeQuestionPageListResponseVO>()
                .successPage(homeQuestionPageListResponseVOS, pageIndex, totalCount, pageSize);
    }

    @Override
    public BaseResponse queryHotTags() {
        List<HomeHotTagResponseVO> homeHotTagResponseVOS = questionTagService.queryHotTags();
        return new BaseResponse<>().success(homeHotTagResponseVOS);
    }

    @Override
    public BaseResponse queryHotQuestions() {
        List<Question> questions = questionService.queryHotQuestions();
        return new BaseResponse<List<Question>>().success(questions);
    }
}
