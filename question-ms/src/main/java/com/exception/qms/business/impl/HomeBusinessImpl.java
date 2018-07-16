package com.exception.qms.business.impl;

import com.exception.qms.business.HomeBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.HomePageQueryResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.HomeListItemTypeEnum;
import com.exception.qms.service.*;
import com.exception.qms.utils.ConstantsUtil;
import com.exception.qms.web.vo.home.HomeHotTagResponseVO;
import com.exception.qms.web.vo.home.QueryHomeItemPageListResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
    private ArticleService articleService;
    @Autowired
    private Mapper mapper;

    @Override
    public HomePageQueryResponse<QueryHomeItemPageListResponseVO> queryHomePageList(String qLimitTime, String aLimitTime) {
//        int totalCount = questionService.queryQuestionTotalCount();

        List<QueryHomeItemPageListResponseVO> homeQuestionPageListResponseVOS = null;

//        if (totalCount > 0) {
        // 根据创建时间倒序
//            String orderByColumn = "create_time";

        if (StringUtils.isBlank(qLimitTime)) {
            qLimitTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE_TIME));
        }

        // 每次拉五条数据
        int limit = 5;

        List<Question> questionList = questionService.queryHomeQuestionList(limit, qLimitTime);

        if (!CollectionUtils.isEmpty(questionList)) {
            // 获取问题 ids
            List<Long> questionIds = questionList.stream().map(Question::getId).collect(Collectors.toList());
            // 关联相关标签信息
//            Map<Long, List<TagResponseVO>> questionIdTagIdsMap = questionTagService.queryTagInfoByQuestionIds(questionIds);

            // 用户信息
            List<Long> createUserIds = questionList.stream().map(Question::getCreateUserId).distinct().collect(Collectors.toList());
            List<User> users = userService.queryUsersByUserIds(createUserIds);
            Map<Long, User> userIdUserMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

            List<Answer> answers = answerService.queryMaxVoteAnswerIdsByQuestionIds(questionIds);
            List<Long> answerIds = answers.stream().map(Answer::getId).collect(Collectors.toList());
            // 问题解决方案内容快照
            List<AnswerDesc> answerDescs = answerService.queryDescByAnswerIds(answerIds);

            Map<Long, Long> questionIdAnswerIdMap = answers.stream().collect(Collectors.toMap(Answer::getQuestionId, Answer::getId));
            Map<Long, AnswerDesc> answerIdAnswerDescMap = answerDescs.stream().collect(Collectors.toMap(AnswerDesc::getAnswerId, answerDesc -> answerDesc));

            // 设置 qLimitTime
            qLimitTime = questionList.get(questionList.size() - 1).getCreateTime().format(DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE_TIME));

            homeQuestionPageListResponseVOS = questionList.stream().map(question -> {
                QueryHomeItemPageListResponseVO homeItemPageListResponseVO = mapper.map(question, QueryHomeItemPageListResponseVO.class);
                homeItemPageListResponseVO.setType(HomeListItemTypeEnum.QUESTION.getCode());

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
                // 问题标签
//                homeQuestionPageListResponseVO.setTags(questionIdTagIdsMap.get(question.getId()));
                return homeItemPageListResponseVO;
            }).collect(Collectors.toList());
//        }
        }

        if (StringUtils.isBlank(aLimitTime)) {
            aLimitTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE_TIME));
        }

        List<Article> articleList = articleService.queryHomeArticleList(limit, aLimitTime);

        // 文章
        if (!CollectionUtils.isEmpty(articleList)) {
            // 设置 aLimitTime
            aLimitTime = articleList.get(articleList.size() - 1).getCreateTime().format(DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE_TIME));

            List<Long> articleIds = articleList.stream().map(Article::getId).collect(Collectors.toList());

            // 用户信息
            List<Long> authorIds = articleList.stream().map(Article::getCreateUserId).collect(Collectors.toList());
            List<User> authors = userService.queryUsersByUserIds(authorIds);
            Map<Long, User> authorIdUserMap = authors.stream().collect(Collectors.toMap(User::getId, user -> user));

            List<ArticleContent> articleContents = articleService.queryArticleContents(articleIds);
            Map<Long, ArticleContent> articleIdArticleContentMap = articleContents.stream().collect(Collectors.toMap(ArticleContent::getArticleId, articleContent -> articleContent));

            // 将查询的文章追加到 list 中
            homeQuestionPageListResponseVOS.addAll(articleList.stream().map(article -> {

                QueryHomeItemPageListResponseVO homeItemPageListResponseVO = mapper.map(article, QueryHomeItemPageListResponseVO.class);
                homeItemPageListResponseVO.setType(HomeListItemTypeEnum.ARTICLE.getCode());
                homeItemPageListResponseVO.setTitleCn(article.getTitle());
                homeItemPageListResponseVO.setTitleImage(article.getTitleImage());
                homeItemPageListResponseVO.setCreateUserId(article.getCreateUserId());

                // 设置快照内容
                Long articleId = article.getId();
                ArticleContent articleContent = articleIdArticleContentMap.get(articleId);
                if (articleContent != null && StringUtils.isNotBlank(articleContent.getContent())) {
                        String content = articleContent.getContent();
                        String shortContent = content.length() > 300 ? content.substring(0, 300) : content;
                        homeItemPageListResponseVO.setShortContent(HtmlUtils.htmlEscape(shortContent) + " ...");
                }

                Long createUserId = article.getCreateUserId();

                boolean isUserExist =
                        createUserId != null
                                && !CollectionUtils.isEmpty(authorIdUserMap)
                                && authorIdUserMap.get(createUserId) != null;

                if (isUserExist) {
                    homeItemPageListResponseVO.setCreateUserName(authorIdUserMap.get(createUserId).getName());
                    homeItemPageListResponseVO.setCreateUserAvatar(authorIdUserMap.get(createUserId).getAvatar());
                    homeItemPageListResponseVO.setCreateUserIntroduction(authorIdUserMap.get(createUserId).getIntroduction());
                }
                return homeItemPageListResponseVO;
            }).collect(Collectors.toList()));
        }

        // list 重新按时间倒序(内存排序)
//        homeQuestionPageListResponseVOS = homeQuestionPageListResponseVOS.stream()
//                .sorted(Comparator.comparing(QueryHomeItemPageListResponseVO::getCreateTime).reversed()).collect(Collectors.toList());

        homeQuestionPageListResponseVOS.sort(Comparator.comparing(QueryHomeItemPageListResponseVO::getCreateTime).reversed());

        return new HomePageQueryResponse<QueryHomeItemPageListResponseVO>()
                .successPage(homeQuestionPageListResponseVOS, 0, 0, 0, qLimitTime, aLimitTime);
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
