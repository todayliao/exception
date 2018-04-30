package com.exception.qms.business.impl;

import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.enums.QuestionTypeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.*;
import com.exception.qms.utils.ConstantsUtil;
import com.exception.qms.utils.IpUtil;
import com.exception.qms.utils.MarkdownUtil;
import com.exception.qms.web.dto.question.request.ChangeQuestionVoteUpRequestDTO;
import com.exception.qms.web.dto.question.request.QuestionViewNumIncreaseRequestDTO;
import com.exception.qms.web.form.question.QuestionForm;
import com.exception.qms.web.form.question.QuestionUpdateForm;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.home.AnswerResponseVO;
import com.exception.qms.web.vo.home.QuestionDetailResponseVO;
import com.exception.qms.web.vo.home.QuestionInfoResponseVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private ExecutorService executorService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private Mapper mapper;

    @Override
    public QuestionDetailResponseVO queryQuestionDetail(Long questionId) {
        Question question = questionService.queryQuestionInfo(questionId);

        // quetion is not exist
        if (question == null) {
            log.warn("The qms is not exsit, questionId:{}", questionId);
            throw new QMSException(QmsResponseCodeEnum.QUESTION_NOT_EXIST);
        }

        QuestionDetailResponseVO questionDetailResponseVO = mapper.map(question, QuestionDetailResponseVO.class);

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

        // question answers
        List<AnswerResponseVO> answerResponseVOS = answerService.queryAnswersByQuestionId(questionId);
        questionDetailResponseVO.setAnswers(answerResponseVOS);
        questionDetailResponseVO.setAnswersCount(CollectionUtils.isEmpty(answerResponseVOS) ? 0 : answerResponseVOS.size());

        // seo
        questionDetailResponseVO.setSeoDescription(questionDesc.getDescriptionCn());
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
            log.warn("The question is not exit, questionId:{}", questionId);
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
        List<Long> tagIds = questionForm.getTagIds().stream()
                .filter(tagId -> tagId != null).distinct().collect(Collectors.toList());

        int size = tagIds.size();
        if (size <= 0 || size > ConstantsUtil.MAX_QUESTION_TAG_COUNT) {
            log.warn("the tagIds size over 5");
            throw new QMSException(QmsResponseCodeEnum.QUESTION_TAGS_OVER);
        }

        // add quesiton
        Question question = new Question();
        question.setTitleCn(questionForm.getTitle());
        question.setCreateUserId(userId);
        question.setType(QuestionTypeEnum.PEOPLE_POST.getCode());
        questionService.addQuestion(question);

        Long questionId = question.getId();

        // add questionDesc
        QuestionDesc questionDesc = new QuestionDesc();
        questionDesc.setDescriptionCn(questionForm.getQuestionDesc());
        questionDesc.setQuestionId(questionId);
        questionDesc.setCreateUserId(userId);
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
        answerService.addAnswer(answer);

        Long answerId = answer.getId();

        // add answerDesc
        AnswerDesc answerDesc = new AnswerDesc();
        answerDesc.setAnswerId(answerId);
        answerDesc.setCreateUserId(userId);
        answerDesc.setDescriptionCn(questionForm.getAnswerDesc());
        answerService.addAnswerDesc(answerDesc);

        // 异步添加/更新 es index
        executorService.execute(() -> questionSearchService.index(questionId));

        return new BaseResponse().success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateQuestion(QuestionUpdateForm questionUpdateForm) {
        Question question = mapper.map(questionUpdateForm, Question.class);
        question.setTitleCn(questionUpdateForm.getTitle());
        questionService.updateQuestion(question);

        // update questionDesc
        QuestionDesc questionDesc = new QuestionDesc();
        questionDesc.setQuestionId(questionUpdateForm.getId());
        questionDesc.setDescriptionCn(questionUpdateForm.getQuestionDesc());
        questionService.updateQuestionDesc(questionDesc);

        // 异步添加/更新 es index
        executorService.execute(() -> questionSearchService.index(questionUpdateForm.getId()));
        return new BaseResponse().success();
    }

    @Override
    public BaseResponse increaseQuestionViewNum(QuestionViewNumIncreaseRequestDTO questionViewNumIncreaseDTO, HttpServletRequest request) {
        log.info("increaseQuestionViewNum, the remote ip: {}", IpUtil.getIpAddr(request));

        String redisKey = String.format("%s_%s", IpUtil.getIpAddr(request), questionViewNumIncreaseDTO.getQuestionId());
        boolean isExisted = redisService.exists(redisKey);

        final long expireSeconds = 1*60*60;

        if (isExisted) {
            log.warn("Can't increase the viewNum of the question, the key already existed: {}", redisKey);
            // expire the key, one hours
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
    public BaseResponse changeQuestionVoteUp(ChangeQuestionVoteUpRequestDTO questionId, HttpSession session) {
        return null;
    }

}
