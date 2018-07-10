package com.exception.qms.business.impl;

import com.exception.qms.business.HomeBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.Answer;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.User;
import com.exception.qms.service.AnswerService;
import com.exception.qms.service.QuestionService;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.TimeUtil;
import com.exception.qms.web.vo.home.HomeHotTagResponseVO;
import com.exception.qms.web.vo.home.QueryHomeItemPageListResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

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
    public PageQueryResponse<QueryHomeItemPageListResponseVO> queryQuestionPageList(Integer pageIndex, Integer pageSize) {
        int totalCount = questionService.queryQuestionTotalCount();

        List<QueryHomeItemPageListResponseVO> homeQuestionPageListResponseVOS = null;

        if (totalCount > 0) {
            // 根据创建时间倒序
            String orderByColumn = "create_time";

            List<Question> questionList = questionService.queryQuestionPageList(pageIndex, pageSize, orderByColumn);

            // 关联相关标签信息
            // 获取问题 ids
            List<Long> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
//            Map<Long, List<TagResponseVO>> questionIdTagIdsMap = questionTagService.queryTagInfoByQuestionIds(questionIds);

            // 用户信息
            List<Long> createUserIds = questionList.stream().map(Question::getCreateUserId).distinct().collect(Collectors.toList());
            List<User> users = userService.queryUsersByUserIds(createUserIds);
            Map<Long, User> userIdUserMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

            List<Answer> answers = answerService.queryMaxVoteAnswerIdsByQuestionIds(questionIds);
            List<Long> answerIds = answers.stream().map(Answer::getId).collect(Collectors.toList());
            // 问题解决方案内容快照
            List<AnswerDesc> answerDescs = answerService.queryDescByAnswerIds(answerIds);

//            if (!CollectionUtils.isEmpty(questionIdTagIdsMap)) {
//
//            }

            Map<Long, Long> questionIdAnswerIdMap = answers.stream().collect(Collectors.toMap(Answer::getQuestionId, Answer::getId));
            Map<Long, AnswerDesc> answerIdAnswerDescMap = answerDescs.stream().collect(Collectors.toMap(AnswerDesc::getAnswerId, answerDesc -> answerDesc));

            homeQuestionPageListResponseVOS = questionList.stream().map(question -> {
                QueryHomeItemPageListResponseVO homeItemPageListResponseVO = mapper.map(question, QueryHomeItemPageListResponseVO.class);

                // 设置快照内容
                Long questionId = question.getId();
                Long answerId = questionIdAnswerIdMap.get(questionId);
                if (answerId != null) {
                    AnswerDesc answerDesc = answerIdAnswerDescMap.get(answerId);
                    if (answerDesc != null) {
                        String descriptionCn = answerDesc.getDescriptionCn();
                        String shortContent = descriptionCn.length() > 300 ? descriptionCn.substring(0, 300) : descriptionCn;
                        homeItemPageListResponseVO.setShortContent(HtmlUtils.htmlEscape(shortContent) + " ...");
                    }
                }

//                homeItemPageListResponseVO.setBeforeTimeStr(TimeUtil.calculateTimeDifference(question.getCreateTime()));
                Long createUserId = question.getCreateUserId();

                boolean isUserExist =
                        createUserId != null
                                && !CollectionUtils.isEmpty(userIdUserMap)
                                && userIdUserMap.get(createUserId) != null;

                if (isUserExist) {
                    homeItemPageListResponseVO.setCreateUserName(userIdUserMap.get(createUserId).getName());
                    homeItemPageListResponseVO.setCreateUserAvatar(userIdUserMap.get(createUserId).getAvatar());
                    homeItemPageListResponseVO.setCreateUserIntroduction(userIdUserMap.get(createUserId).getIntroduction());
                }
//                homeQuestionPageListResponseVO.setTags(questionIdTagIdsMap.get(question.getId()));
                return homeItemPageListResponseVO;
            }).collect(Collectors.toList());

        }

        return new PageQueryResponse<QueryHomeItemPageListResponseVO>()
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
