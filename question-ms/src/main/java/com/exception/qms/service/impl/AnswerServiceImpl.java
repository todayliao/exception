package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.enums.VoteOperationTypeEnum;
import com.exception.qms.service.AnswerService;
import com.exception.qms.utils.MarkdownUtil;
import com.exception.qms.web.vo.home.AnswerResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private AnswerDescMapper answerDescMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AnswerVoteUserRelMapper answerVoteUserRelMapper;
    @Autowired
    private AnswerEditHistoryMapper answerEditHistoryMapper;
    @Autowired
    private Mapper mapper;

    @Override
    public List<AnswerResponseVO> queryAnswersByQuestionId(Long questionId, Long userId) {
        List<Answer> answers = answerMapper.queryByQuestionId(questionId);
        List<Long> answerIds = answers.stream().map(Answer::getId).collect(Collectors.toList());
        List<AnswerDesc> answerDescs = answerDescMapper.queryByAnswerIds(answerIds);

        Map<Long, AnswerDesc> map = answerDescs.stream()
                .collect(Collectors.toMap(AnswerDesc::getAnswerId, answerDesc -> answerDesc));

        // query answer's user
        List<Long> userIds = answers.stream().map(Answer::getCreateUserId).collect(Collectors.toList());
        List<Long> latestEditorUserIds = answers.stream().map(Answer::getLatestEditorUserId).collect(Collectors.toList());
        userIds.addAll(latestEditorUserIds);
        List<User> users = userMapper.queryUsersByUserIds(userIds);
        Map<Long, User> userIdUserMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        List<AnswerResponseVO> answerResponseVOS = answers.stream().map(answer -> {
            AnswerResponseVO  answerResponseVO = mapper.map(answer, AnswerResponseVO.class);
            // 方案创建时间日期处理
            answerResponseVO.setCreateDateStr(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(answer.getCreateTime()));
            answerResponseVO.setCreateTimeStr(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:hh:ss").format(answer.getCreateTime()));

            if (!CollectionUtils.isEmpty(map) && map.get(answer.getId()) != null) {
                String descriptionCn = map.get(answer.getId()).getDescriptionCn();
                answerResponseVO.setDescriptionCnHtml(MarkdownUtil.parse2Html(descriptionCn));

                // answer's user
                if (!CollectionUtils.isEmpty(userIdUserMap)) {
                    answerResponseVO.setUser(userIdUserMap.get(answer.getCreateUserId()));
                    // 方案的最新编辑人
                    User latestEditorUser = userIdUserMap.get(answer.getLatestEditorUserId());
                    if (latestEditorUser != null) {
                        answerResponseVO.setLatestEditorUserAvatar(latestEditorUser.getAvatar());
                        answerResponseVO.setLatestEditorUserName(latestEditorUser.getName());
                        AnswerEditHistory answerEditHistory = answerEditHistoryMapper.queryLatestRecordByAnswerIdAndUserId(answer.getId(), latestEditorUser.getId());
                        if (answerEditHistory != null) {
                            answerResponseVO.setLatestEditorTimeStr(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:hh:ss").format(answerEditHistory.getCreateTime()));
                        }
                    }
                }
                // default is false
                answerResponseVO.setIsCurrentUserVoteUp(false);
            }
            return answerResponseVO;
        }).collect(Collectors.toList());

        // query answer vote user rel by answerIds and current userId
        if (userId != null) {
            List<AnswerVoteUserRel> answerVoteUserRels = answerVoteUserRelMapper.queryByAnswerIdsAndVoteUserId(answerIds, userId, VoteOperationTypeEnum.UP.getCode());
            Map<Long, AnswerVoteUserRel> answerIdAnswerVoteUserRelMap = answerVoteUserRels.stream().collect(Collectors.toMap(AnswerVoteUserRel::getAnswerId, answerVoteUserRel -> answerVoteUserRel));
            answerResponseVOS = answerResponseVOS.stream().map(answerResponseVO -> {
                if (!CollectionUtils.isEmpty(answerIdAnswerVoteUserRelMap)) {
                    boolean isCurrentUserVoteUp = answerIdAnswerVoteUserRelMap.get(answerResponseVO.getId()) != null;
                    answerResponseVO.setIsCurrentUserVoteUp(isCurrentUserVoteUp);
                }
                return answerResponseVO;
            }).collect(Collectors.toList());
        }

        return answerResponseVOS;
    }

    @Override
    public int addAnswer(Answer answer) {
        return answerMapper.insert(answer);
    }

    @Override
    public int addAnswerDesc(AnswerDesc answerDesc) {
        return answerDescMapper.insert(answerDesc);
    }

    @Override
    public Answer queryAnswerInfo(long answerId) {
        return answerMapper.selectByPrimaryKey(answerId);
    }

    @Override
    public AnswerDesc queryAnswerDescInfo(Long answerId) {
        return answerDescMapper.queryAnswerDescInfo(answerId);
    }

    @Override
    public List<Answer> queryByQuestionIds(List<Long> questionIds) {
        return answerMapper.queryByQuestionIds(questionIds);
    }

    @Override
    public int updateAnswer(Answer answer) {
        return answerMapper.updateByPrimaryKeySelective(answer);
    }

    @Override
    public int updateAnswerDesc(AnswerDesc answerDesc) {
        return answerDescMapper.updateByPrimaryKeySelective(answerDesc);
    }

    @Override
    public int voteUpAnswer(long answerId) {
        return answerMapper.voteUpAnswer(answerId);
    }

    @Override
    public int voteDownAnswer(long answerId) {
        return answerMapper.voteDownAnswer(answerId);
    }

    @Override
    public int addAnswerEditHistory(AnswerEditHistory answerEditHistory) {
        return answerEditHistoryMapper.insert(answerEditHistory);
    }
}
