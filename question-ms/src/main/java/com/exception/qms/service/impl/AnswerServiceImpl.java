package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.Answer;
import com.exception.qms.domain.entity.AnswerDesc;
import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.mapper.AnswerDescMapper;
import com.exception.qms.domain.mapper.AnswerMapper;
import com.exception.qms.domain.mapper.UserMapper;
import com.exception.qms.service.AnswerService;
import com.exception.qms.utils.MarkdownUtil;
import com.exception.qms.web.vo.home.AnswerResponseVO;
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
    private Mapper mapper;

    @Override
    public List<AnswerResponseVO> queryAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerMapper.queryByQuestionId(questionId);
        List<Long> answerIds = answers.stream().map(Answer::getId).collect(Collectors.toList());
        List<AnswerDesc> answerDescs = answerDescMapper.queryByAnswerIds(answerIds);

        Map<Long, AnswerDesc> map = answerDescs.stream()
                .collect(Collectors.toMap(AnswerDesc::getAnswerId, answerDesc -> answerDesc));

        // query answer's user
        List<Long> createUserIds = answers.stream().map(Answer::getCreateUserId).collect(Collectors.toList());
        List<User> users = userMapper.queryUsersByUserIds(createUserIds);
        Map<Long, User> userIdUserMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        List<AnswerResponseVO> answerResponseVOS = answers.stream().map(answer -> {
            AnswerResponseVO  answerResponseVO = mapper.map(answer, AnswerResponseVO.class);
            if (!CollectionUtils.isEmpty(map) && map.get(answer.getId()) != null) {
                String descriptionCn = map.get(answer.getId()).getDescriptionCn();
                answerResponseVO.setDescriptionCnHtml(MarkdownUtil.parse2Html(descriptionCn));
                // answer's user
                if (!CollectionUtils.isEmpty(userIdUserMap)) {
                    answerResponseVO.setUser(userIdUserMap.get(answer.getCreateUserId()));
                }
            }
            return answerResponseVO;
        }).collect(Collectors.toList());
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
    public AnswerDesc queryAnswerDescInfo(Long answerId) {
        return answerDescMapper.queryAnswerDescInfo(answerId);
    }

    @Override
    public int updateAnswerDesc(AnswerDesc answerDesc) {
        return answerDescMapper.updateByPrimaryKeySelective(answerDesc);
    }
}
