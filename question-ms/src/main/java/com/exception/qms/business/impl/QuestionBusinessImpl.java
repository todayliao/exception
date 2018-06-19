package com.exception.qms.business.impl;

import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.*;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.*;
import com.exception.qms.utils.*;
import com.exception.qms.web.dto.question.request.ChangeQuestionVoteUpRequestDTO;
import com.exception.qms.web.dto.question.request.QuestionViewNumIncreaseRequestDTO;
import com.exception.qms.web.form.question.QuestionForm;
import com.exception.qms.web.form.question.QuestionUpdateForm;
import com.exception.qms.web.vo.common.QuestionListItemResponseVO;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.home.AnswerResponseVO;
import com.exception.qms.web.vo.home.QueryHomeQuestionPageListResponseVO;
import com.exception.qms.web.vo.home.QuestionDetailResponseVO;
import com.exception.qms.web.vo.home.QuestionInfoResponseVO;
import com.exception.qms.web.vo.tag.QueryQuestionTagPageListResponseVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class QuestionBusinessImpl implements QuestionBusiness {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionTagService questionTagService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionSearchService questionSearchService;
    @Autowired
    private QuestionVoteUserService questionVoteUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionEditHistoryService questionEditHistoryService;
    @Autowired
    private BaiduLinkPushService baiduLinkPushService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Mapper mapper;

    @Override
    public QuestionDetailResponseVO queryQuestionDetail(Long questionId, Long userId) {
        Question question = questionService.queryQuestionInfo(questionId);

        // quetion is not exist
        if (question == null) {
            log.warn("The qms is not exsit, questionId:{}", questionId);
            throw new QMSException(QmsResponseCodeEnum.QUESTION_NOT_EXIST);
        }

        QuestionDetailResponseVO questionDetailResponseVO = mapper.map(question, QuestionDetailResponseVO.class);
        // 日期格式转换
        questionDetailResponseVO.setCreateDateStr(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(question.getCreateTime()));
        questionDetailResponseVO.setCreateTimeStr(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:hh:ss").format(question.getCreateTime()));

        // quetion desc
        QuestionDesc questionDesc = questionService.queryQuestionDesc(questionId);
        questionDetailResponseVO.setQuestionDescHtml(questionDesc != null ?
                MarkdownUtil.parse2Html(questionDesc.getDescriptionCn()) : null);

        // question tags
        Map<Long, List<TagResponseVO>> questionIdTagsMap = questionTagService.queryTagInfoByQuestionIds(Lists.newArrayList(questionId));
        if (!CollectionUtils.isEmpty(questionIdTagsMap)
                && questionIdTagsMap.get(questionId) != null) {
            questionDetailResponseVO.setTags(questionIdTagsMap.get(questionId));
        }

        // 当前用户是否已经点赞
        boolean isCurrentUserVoteUp = false;
        if (userId != null) {
            isCurrentUserVoteUp = questionVoteUserService.isCurrentUserVoteUp(userId, questionId);
        }
        questionDetailResponseVO.setIsCurrentUserVoteUp(isCurrentUserVoteUp);

        // 问题创建者，编辑者
        List<Long> userIds = Lists.newArrayList();
        userIds.add(question.getCreateUserId());
        if (question.getLatestEditorUserId() != null
                && question.getLatestEditorUserId() != 0) {
            userIds.add(question.getLatestEditorUserId());
        }
        List<User> users = userService.queryUsersByUserIds(userIds);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        if (!CollectionUtils.isEmpty(userMap)) {
            User createUser = userMap.get(question.getCreateUserId());
            User latestEditUser = userMap.get(question.getLatestEditorUserId());
            questionDetailResponseVO.setCreateUserAvatar(createUser == null ? null : createUser.getAvatar());
            questionDetailResponseVO.setCreateUserName(createUser == null ? null : createUser.getName());

            // 存在最新的编辑者
            if (latestEditUser != null) {
                questionDetailResponseVO.setLatestEditorUserAvatar(latestEditUser.getAvatar());
                questionDetailResponseVO.setLatestEditorUserName(latestEditUser.getName());
                // 查询最新编辑者的编辑时间
                QuestionEditHistory questionEditHistory = questionEditHistoryService.query(questionId, latestEditUser.getId());
                if (questionEditHistory != null) {
                    questionDetailResponseVO.setLatestEditorTimeStr(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:hh:ss").format(questionEditHistory.getCreateTime()));
                }
            }
        }

        // question answers
        List<AnswerResponseVO> answerResponseVOS = answerService.queryAnswersByQuestionId(questionId, userId);
        questionDetailResponseVO.setAnswers(answerResponseVOS);
        questionDetailResponseVO.setAnswersCount(CollectionUtils.isEmpty(answerResponseVOS) ? 0 : answerResponseVOS.size());

        // seo
        String descriptionCn = questionDesc.getDescriptionCn();
        // description 最多显示 120 字符
        if (descriptionCn.length() > 120) {
            questionDetailResponseVO.setSeoDescription(questionDesc.getDescriptionCn().substring(0, 120));
        } else {
            questionDetailResponseVO.setSeoDescription(questionDesc.getDescriptionCn());
        }
        List<TagResponseVO> tagResponseVOS = questionDetailResponseVO.getTags();
        if (!CollectionUtils.isEmpty(tagResponseVOS)) {
            questionDetailResponseVO.setSeoKeywords(
                    tagResponseVOS.stream().map(TagResponseVO::getTagName).collect(Collectors.joining(","))
            );
        }

        return questionDetailResponseVO;
    }

    @Override
    public QuestionInfoResponseVO queryQuestionInfo(Long questionId) {
        Question question = questionService.queryQuestionInfo(questionId);

        // quetion is not exist
        if (question == null) {
            log.warn("The question is not exit, questionId: {}", questionId);
            throw new QMSException(QmsResponseCodeEnum.QUESTION_NOT_EXIST);
        }

        QuestionInfoResponseVO questionInfoResponseVO = mapper.map(question, QuestionInfoResponseVO.class);

        // quetion desc
        QuestionDesc questionDesc = questionService.queryQuestionDesc(questionId);
        questionInfoResponseVO.setQuestionDesc(questionDesc.getDescriptionCn());

        return questionInfoResponseVO;
    }

    /**
     * 添加问题（包括标题，问题描述，以及解决方案）
     *
     * @param questionForm
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse addQuestion(QuestionForm questionForm, Long userId) {
        // 判断问题标题是否包含 ？
        if (!questionForm.getTitle().contains("?") && !questionForm.getTitle().contains("？")) {
            log.warn("the question not contains ?");
            throw new QMSException(QmsResponseCodeEnum.NOT_CONTAIN_QUESTION_MARK);
        }

        List<Long> tagIds = questionForm.getTagIds().stream()
                .filter(tagId -> tagId != null).distinct().collect(Collectors.toList());

        int size = tagIds.size();
        if (size <= 0 || size > ConstantsUtil.MAX_QUESTION_TAG_COUNT) {
            log.warn("the tagIds size over 5");
            throw new QMSException(QmsResponseCodeEnum.QUESTION_TAGS_OVER);
        }

        // add quesiton
        Question question = new Question();
        question.setTitleCn(StringUtil.spacingText(questionForm.getTitle()));
        question.setCreateUserId(userId);
        question.setType(QuestionTypeEnum.PEOPLE_POST.getCode());
        // 第一次添加问题，不存在最新编辑的用户，默认为0
        question.setLatestEditorUserId(0L);
        questionService.addQuestion(question);

        Long questionId = question.getId();

        // add questionDesc
        QuestionDesc questionDesc = new QuestionDesc();
        // 判断用户是否填写问题描述
        if (questionForm.getQuestionDesc().isEmpty()) {
            questionDesc.setDescriptionCn(StringUtil.spacingText(questionForm.getTitle()));
        } else {
            questionDesc.setDescriptionCn(StringUtil.spacingText(questionForm.getQuestionDesc()));
        }
        questionDesc.setQuestionId(questionId);
        questionService.addQuestionDesc(questionDesc);

        // batch add questionTagRel
        List<QuestionTagRel> questionTagRels = Lists.newArrayList();
        tagIds.forEach(tagId -> {
            QuestionTagRel questionTagRel = new QuestionTagRel();
            questionTagRel.setTagId(tagId);
            questionTagRel.setQuestionId(questionId);
            questionTagRels.add(questionTagRel);
        });
        questionTagService.batchAddQuestionTagRel(questionTagRels);

        // add answer
        Answer answer = new Answer();
        answer.setCreateUserId(userId);
        answer.setIsAccepted(true);
        answer.setQuestionId(questionId);
        // 初次添加解决方案，最近一次编辑者的id默认为0
        answer.setLatestEditorUserId(0L);
        answerService.addAnswer(answer);

        Long answerId = answer.getId();

        // add answerDesc
        AnswerDesc answerDesc = new AnswerDesc();
        answerDesc.setAnswerId(answerId);
        answerDesc.setDescriptionCn(StringUtil.spacingText(questionForm.getAnswerDesc()));
        answerService.addAnswerDesc(answerDesc);

        // 异步添加记录贡献
        executorService.execute(() -> userService.addQuestionContribution(questionId, userId, UserQuestionContributionTypeEnum.CREATE.getCode()));
        executorService.execute(() -> userService.addAnswerContribution(answerId, userId, UserAnswerContributionTypeEnum.CREATE.getCode()));

        // 异步添加/更新 es index
        executorService.execute(() -> questionSearchService.index(questionId));

        // 异步推送链接给百度，加快收录速度
        executorService.execute(() -> baiduLinkPushService.pushQuestionDetailPageLink(questionId));

        return new BaseResponse().success(questionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateQuestion(QuestionUpdateForm questionUpdateForm, Long userId) {
        if (userId == null) {
            log.error("the userId is null");
            throw new QMSException(QmsResponseCodeEnum.USER_IS_NULL);
        }

        // 判断问题标题是否包含 ？
        if (!questionUpdateForm.getTitle().contains("?") && !questionUpdateForm.getTitle().contains("？")) {
            log.warn("the question not contains ?");
            throw new QMSException(QmsResponseCodeEnum.NOT_CONTAIN_QUESTION_MARK);
        }

        // 老数据入问题历史表
        long questionId = questionUpdateForm.getId();
        Question tmpQuestion = questionService.queryQuestionInfo(questionId);
        QuestionDesc tmpQuestionDesc = questionService.queryQuestionDesc(questionId);

        // 判断修改内容是否发生实质的改变
        boolean isTitleAndDescNotChanged = tmpQuestion.getTitleCn().trim().equals(questionUpdateForm.getTitle().trim())
                && tmpQuestionDesc.getDescriptionCn().trim().equals(questionUpdateForm.getQuestionDesc().trim());

        if (isTitleAndDescNotChanged) {
            log.warn("the question title and description has't changed, userId: {}, questionId: {}", userId, questionUpdateForm.getId());
            throw new QMSException(QmsResponseCodeEnum.UPDATE_CONTENT_NOT_CHANGE);
        }

        QuestionEditHistory questionEditHistory = new QuestionEditHistory();
        // 设置历史数据的创建者
        if (tmpQuestion.getLatestEditorUserId() == 0) {
            questionEditHistory.setCreateUserId(tmpQuestion.getCreateUserId());
        } else {
            questionEditHistory.setCreateUserId(tmpQuestion.getLatestEditorUserId());
        }
        questionEditHistory.setQuestionId(questionId);
        questionEditHistory.setTitleCn(tmpQuestion.getTitleCn());
        questionEditHistory.setQuestionCreateTime(tmpQuestion.getUpdateTime());

        questionEditHistory.setDescriptionCn(tmpQuestionDesc.getDescriptionCn());

        questionService.addQuestionEditHistory(questionEditHistory);

        Question question = mapper.map(questionUpdateForm, Question.class);
        question.setTitleCn(StringUtil.spacingText(questionUpdateForm.getTitle()));
        // 设置最新的编辑人
        question.setLatestEditorUserId(userId);
        questionService.updateQuestion(question);

        // update questionDesc
        QuestionDesc questionDesc = new QuestionDesc();
        questionDesc.setQuestionId(questionUpdateForm.getId());
        questionDesc.setDescriptionCn(StringUtil.spacingText(questionUpdateForm.getQuestionDesc()));
        questionService.updateQuestionDesc(questionDesc);

        // 异步添加记录贡献
        executorService.execute(() -> userService.addQuestionContribution(questionId, userId, UserQuestionContributionTypeEnum.EDIT.getCode()));

        // 异步添加/更新 es index
        executorService.execute(() -> questionSearchService.index(questionUpdateForm.getId()));
        return new BaseResponse().success(questionId);
    }

    @Override
    public BaseResponse increaseQuestionViewNum(QuestionViewNumIncreaseRequestDTO questionViewNumIncreaseDTO, HttpServletRequest request) {
        log.info("increaseQuestionViewNum, the remote ip: {}", IpUtil.getIpAddr(request));

        String redisKey = String.format("%s_%s", IpUtil.getIpAddr(request), questionViewNumIncreaseDTO.getQuestionId());
        boolean isExisted = redisService.exists(redisKey);

        final long expireSeconds = 1*60*60;

        if (isExisted) {
            log.warn("Can't increase the viewNum of the question, the key already existed on the cache: {}", redisKey);
            // expire the key, one hour
            redisService.expire(redisKey, expireSeconds);
            return new BaseResponse().fail();
        }

        int count = questionService.increaseQuestionViewNum(questionViewNumIncreaseDTO.getQuestionId());

        if (count > 0) {
            redisService.set(redisKey, "", expireSeconds);
            return new BaseResponse().success();
        }
        return new BaseResponse().fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse changeQuestionVoteUp(ChangeQuestionVoteUpRequestDTO changeQuestionVoteUpRequestDTO, Long userId) {
        int operationType = changeQuestionVoteUpRequestDTO.getOperationType();
        long questionId = changeQuestionVoteUpRequestDTO.getQuestionId();

        if (userId == null) {
            throw new QMSException(QmsResponseCodeEnum.USER_IS_NULL);
        }

        VoteOperationTypeEnum voteOperationTypeEnum = VoteOperationTypeEnum.codeOf(operationType);

        switch (voteOperationTypeEnum) {
            case UP:
                questionService.voteUpQuestion(questionId);
                // 添加问题和点赞用户的绑定信息
                try {
                    questionVoteUserService.addQuestionVoteUserRecord(questionId, operationType, userId);
                } catch (DuplicateKeyException e) {
                    log.error("duplicate key ==> questionId: {}, userId: {}, operationType: {}", questionId, userId, operationType);
                    throw new QMSException(QmsResponseCodeEnum.ALREADY_VOTE_UP);
                }
                break;
            case DOWN:
                questionService.voteDownQuestion(questionId);
                // 添加问题和踩用户的绑定信息
                try {
                    questionVoteUserService.addQuestionVoteUserRecord(questionId, operationType, userId);
                } catch (DuplicateKeyException e) {
                    log.error("duplicate key ==> questionId: {}, userId: {}, operationType: {}", questionId, userId, operationType);
                    throw new QMSException(QmsResponseCodeEnum.ALREADY_VOTE_DOWN);
                }
                break;
            default:
                throw new QMSException(QmsResponseCodeEnum.PARAM_ERROR);
        }

        return new BaseResponse().success();
    }

    @Override
    public QueryQuestionTagPageListResponseVO queryQuestionTagPageList(Long tagId, Integer pageIndex, Integer pageSize, String tab) {
        // tag info
        Tag tag = tagService.queryById(tagId);

        if (tag == null) {
            log.error("the tagId is not exited, tagId: {}", tagId);
            throw new QMSException(QmsResponseCodeEnum.TAG_NOT_EXIST);
        }

        QueryQuestionTagPageListResponseVO queryQuestionTagPageListResponseVO = new QueryQuestionTagPageListResponseVO();
        queryQuestionTagPageListResponseVO.setId(tag.getId());
        queryQuestionTagPageListResponseVO.setName(tag.getName());
        queryQuestionTagPageListResponseVO.setQuestionCount(tag.getQuestionCount());
        queryQuestionTagPageListResponseVO.setDescriptionCn(tag.getDescriptionCn());

        int totalCount = 0;
        List<QuestionListItemResponseVO> questionListItemResponseVOS = null;

        List<QuestionTagRel> questionTagRels = questionTagService.queryByTagId(tagId);

        if (!CollectionUtils.isEmpty(questionTagRels)) {
            List<Long> questionTagIds = questionTagRels.stream().map(QuestionTagRel::getQuestionId).collect(Collectors.toList());

            totalCount = questionService.queryQuestionTagTotalCount(questionTagIds);

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

                List<Question> questions = questionService.queryQuestionTagPageList(questionTagIds, pageIndex, pageSize, orderByColumn);

                // 关联相关标签信息
                // 获取问题 ids
                List<Long> questionIds = questions.stream().map(Question::getId).collect(Collectors.toList());
                Map<Long, List<TagResponseVO>> questionIdTagIdsMap = questionTagService.queryTagInfoByQuestionIds(questionIds);

                // 用户信息
                List<Long> createUserIds = questions.stream().map(Question::getCreateUserId).distinct().collect(Collectors.toList());
                List<User> users = userService.queryUsersByUserIds(createUserIds);
                Map<Long, User> userIdUserMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

                if (!CollectionUtils.isEmpty(questionIdTagIdsMap)) {
                    questionListItemResponseVOS = questions.stream().map(question -> {
                        QuestionListItemResponseVO questionListItemResponseVO = mapper.map(question, QuestionListItemResponseVO.class);
                        questionListItemResponseVO.setBeforeTimeStr(TimeUtil.calculateTimeDifference(question.getCreateTime()));
                        Long createUserId = question.getCreateUserId();

                        boolean isUserExist =
                                createUserId != null
                                        && !CollectionUtils.isEmpty(userIdUserMap)
                                        && userIdUserMap.get(createUserId) != null;

                        if (isUserExist) {
                            questionListItemResponseVO.setCreateUserName(userIdUserMap.get(createUserId).getName());
                            questionListItemResponseVO.setCreateUserAvatar(userIdUserMap.get(createUserId).getAvatar());
                        }
                        questionListItemResponseVO.setTags(questionIdTagIdsMap.get(question.getId()));
                        return questionListItemResponseVO;
                    }).collect(Collectors.toList());
                }
            }
        }

        return (QueryQuestionTagPageListResponseVO) queryQuestionTagPageListResponseVO
                .successPage(questionListItemResponseVOS, pageIndex, totalCount, pageSize);
    }

}
