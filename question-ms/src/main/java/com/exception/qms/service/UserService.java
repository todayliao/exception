package com.exception.qms.service;

import com.exception.qms.domain.entity.User;

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
}
