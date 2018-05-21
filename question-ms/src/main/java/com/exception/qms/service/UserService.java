package com.exception.qms.service;

import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.entity.UserAnswerContribution;
import com.exception.qms.domain.entity.UserQuestionContribution;

import java.time.LocalDate;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface UserService {

    /**
     * 根据 userId 批量获取用户信息
     *
     * @param userIds
     * @return
     */
    List<User> queryUsersByUserIds(List<Long> userIds);

    User queryByUserName(String userName);

    int queryUserPageListCount();

    List<User> queryUserPageList(Integer pageIndex, Integer pageSize);

    /**
     * 更新用户最后一次登录时间为当前时间
     *
     * @return
     */
    int updateLastLoginTime(Long userId);

    List<UserQuestionContribution> queryUserQuestionContribution(long userId, LocalDate today, LocalDate lastYearToday);

    List<UserAnswerContribution> queryUserAnswerContribution(Long userId, LocalDate today, LocalDate lastYearToday);

    /**
     * 添加问题的贡献记录
     *
     * @param questionId
     * @return
     */
    int addQuestionContribution(Long questionId, Long userId, int type);

    /**
     * 添加解决方案的贡献记录
     * @return
     */
    int addAnswerContribution(Long answerId, Long userId, int type);
}
